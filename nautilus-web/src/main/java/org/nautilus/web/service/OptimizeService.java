package org.nautilus.web.service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.util.Strings;
import org.nautilus.core.algorithm.Builder;
import org.nautilus.core.algorithm.GA;
import org.nautilus.core.algorithm.pcansgaii.PCANSGAII;
import org.nautilus.core.algorithm.pcansgaii.PCANSGAII.ProblemListener;
import org.nautilus.core.encoding.NSolution;
import org.nautilus.core.listener.AlgorithmListener;
import org.nautilus.core.listener.OnProgressListener;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.core.util.Converter;
import org.nautilus.core.util.Formatter;
import org.nautilus.core.util.SolutionAttribute;
import org.nautilus.plugin.extension.AlgorithmExtension;
import org.nautilus.plugin.extension.CrossoverExtension;
import org.nautilus.plugin.extension.MutationExtension;
import org.nautilus.plugin.extension.ProblemExtension;
import org.nautilus.plugin.extension.SelectionExtension;
import org.nautilus.web.persistence.dto.ExecutionQueueDTO;
import org.nautilus.web.persistence.dto.UserDTO;
import org.nautilus.web.persistence.model.Execution;
import org.nautilus.web.persistence.repository.ExecutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;

@Component
public class OptimizeService {

    @Autowired
    private List<Execution> pendingExecutions;
    
    @Autowired
    private List<ExecutionQueueDTO> runningExecutions;
    
    @Autowired
    private ExecutorService executorService;
    
    @Autowired
    private PluginService pluginService;
    
    @Autowired
    private FileService fileService;
    
    @Autowired
    private SimpMessageSendingOperations messaging;
    
    @Autowired
    private ExecutionRepository executionRepository;
    
    @Autowired
    private ExecutionService executionService;
    
    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private Map<String, List<String>> loggedUsers;
    
    @Scheduled(fixedRate = 1000)
    public void execute() {
        
        Iterator<Execution> it = pendingExecutions.iterator();
        
        while (it.hasNext()) {
            
            Execution execution = it.next();
            
            UserDTO userDTO = userService.findUserDTOById(execution.getUserId());
            
            String email = userDTO.getEmail();
            
            String timeZone = userService.findByEmail(email).getTimeZone();
            
            ExecutionQueueDTO itemQueue = new ExecutionQueueDTO();
            
            itemQueue.setId(execution.getId());
            itemQueue.setUserId(execution.getUserId());
            itemQueue.setCreationDate(execution.getCreationDate());
            itemQueue.setTitle(Formatter.date(execution.getCreationDate(), timeZone));
            itemQueue.setProgress(0.0);
            
            setStatus(itemQueue, "execution.status.pending");
            
            runningExecutions.add(itemQueue);
            
            pendingExecutions.remove(execution);
            
            sendAppend(email, itemQueue);
            
            executorService.submit(getTask(email, execution, itemQueue));
        }
    }
    
    @SuppressWarnings("unchecked")
    public Callable<Execution> getTask(String email, Execution execution, ExecutionQueueDTO itemQueue) {
        
        Callable<Execution> callableTask = () -> {
            
            sendProgress(email, itemQueue);
            
            setStatus(itemQueue, "execution.status.initializing");
            sendProgress(email, itemQueue);
            
            ProblemExtension problemExtension = pluginService.getProblemById(execution.getProblemId());
            
            Path path = fileService.getInstance(execution.getProblemId(), execution.getInstance());
            
            List<AbstractObjective> objectives = problemExtension.getObjectiveByIds(execution.getObjectiveIds());
            
            Instance instance = problemExtension.getInstance(path);
            
            // Factories
            
            AlgorithmExtension algorithmExtension = pluginService.getAlgorithmExtensionById(execution.getAlgorithmId());
            SelectionExtension selectionExtension = pluginService.getSelectionExtensionById(execution.getSelectionId());
            CrossoverExtension crossoverExtension = pluginService.getCrossoverExtensionById(execution.getCrossoverId());
            MutationExtension mutationExtension = pluginService.getMutationExtensionById(execution.getMutationId());
            
            // Builder
            
            Builder builder = new Builder();
            
            builder.setPopulationSize(execution.getPopulationSize());
            builder.setMaxEvaluations(execution.getMaxEvaluations());
            builder.setProblem(problemExtension.getProblem(instance, objectives));
            builder.setReferencePoints(Converter.toReferencePoints(execution.getReferencePoints()));
            builder.setEpsilon(execution.getEpsilon());
            
            builder.setInitialPopulation(getInitialPopulation(builder.getProblem(), execution.getLastExecutionId()));
            
            builder.setSelection(selectionExtension.getSelection());
            builder.setCrossover(crossoverExtension.getCrossover(execution.getCrossoverProbability(), execution.getCrossoverDistribution()));
            builder.setMutation(mutationExtension.getMutation(execution.getMutationProbability(), execution.getMutationDistribution()));
            
            // Algorithm
            
            try {
                
                List<NSolution<?>> rawSolutions = null;
                
                setStatus(itemQueue, "execution.status.optimizing");
                sendProgress(email, itemQueue);
                
                Algorithm<?> algorithm = algorithmExtension.getAlgorithm(builder);
    
                AlgorithmListener alg = (AlgorithmListener) algorithm;
    
                alg.setOnProgressListener(new OnProgressListener() {
    
                    @Override
                    public void onProgress(double progress) {
                        
                        if (itemQueue.getStatus().equalsIgnoreCase("Cancelled")) {
                            throw new RuntimeException("Cancelled");
                        }
                        
                        itemQueue.setProgress(progress);
                        
                        sendProgress(email, itemQueue);
                    }
                });
                
                if (algorithm instanceof PCANSGAII) {

                    ((PCANSGAII<?>) algorithm).setProblemListener(new ProblemListener() {

                        @Override
                        public Problem<?> getNewProblem(List<String> objectives) {
                            
                            List<AbstractObjective> objs = problemExtension.getObjectiveByIds(objectives);
                            
                            return problemExtension.getProblem(instance, objs);
                        }
                    });
                }
                
                long initTime = System.currentTimeMillis();
                
                algorithm.run();
               
                long computingTime = System.currentTimeMillis() - initTime ;
                
                if (algorithm instanceof GA) {
                    rawSolutions = (List<NSolution<?>>) (Object) Arrays.asList(algorithm.getResult());
                } else if (algorithm instanceof PCANSGAII) {

                    rawSolutions = new ArrayList<>();

                    Problem originalProblem = builder.getProblem();
                    
                    for (NSolution<?> sol : (List<NSolution<?>>) algorithm.getResult()) {

                        NSolution<?> newSolution = (NSolution<?>) Converter.toSolutionWithOutObjectives(originalProblem, sol);
                        
                        originalProblem.evaluate(newSolution);
                        
                        rawSolutions.add(newSolution);
                    }

                } else {
                    rawSolutions = (List<NSolution<?>>) algorithm.getResult();
                }
                
                setStatus(itemQueue, "execution.status.preparing");
                sendProgress(email, itemQueue);
    
                for (int i = 0; i < rawSolutions.size(); i++) {
                    rawSolutions.get(i).getAttributes().clear();
                    rawSolutions.get(i).setAttribute(SolutionAttribute.ID, String.valueOf(i));
                    rawSolutions.get(i).setAttribute(SolutionAttribute.OPTIMIZED_OBJECTIVES, Converter.toJson(execution.getObjectiveIds()));
                }
                
                execution.setSolutions(rawSolutions);
                execution.setExecutionTime(computingTime);
                
                setStatus(itemQueue, "execution.status.saving");
                sendProgress(email, itemQueue);
                
                executionRepository.save(execution);
                
                setStatus(itemQueue, "execution.status.done");
                itemQueue.setProgress(100);
                sendProgress(email, itemQueue);
            } catch (Exception ex) {
                
                ex.printStackTrace();
                
                executionRepository.deleteById(execution.getId());

                itemQueue.setStatus(ex.getMessage());
                itemQueue.setProgress(100);

                sendError(email, itemQueue);
            }
            
            runningExecutions.remove(itemQueue);
            
            return null;
        };
        
        return callableTask;
    }
    
    public List<NSolution<?>> getInitialPopulation(Problem<?> problem, String previousExecutionId) {

        if (Strings.isBlank(previousExecutionId)) {
            return null;
        }

        Execution previousExecution = executionService.findExecutionById(previousExecutionId);

        List<NSolution<?>> initialPopulation = new ArrayList<>();

        for (NSolution<?> sol : previousExecution.getSolutions()) {

            NSolution<?> copy = (NSolution<?>) sol.copy();

            copy.setObjectives(new double[problem.getNumberOfObjectives()]);

            initialPopulation.add(copy);
        }

        return initialPopulation;
    }
    
    public void cancel(String executionId) {
    
        for(ExecutionQueueDTO item : runningExecutions) {
            
            if(item.getId().equalsIgnoreCase(executionId)) {
                item.setStatus("Cancelled");
            }
        }
    }
    
    public void sendError(String email, ExecutionQueueDTO itemQueue) {
        
        List<String> sessions = loggedUsers.get(email);

        if (sessions == null) {
            return;
        }
        
        for (String sessionId : sessions) {
            messaging.convertAndSendToUser(sessionId,"/execution/queue/error", itemQueue);
        }
    }
    
    public void sendAppend(String email, ExecutionQueueDTO itemQueue) {
        
        List<String> sessions = loggedUsers.get(email);

        if (sessions == null) {
            return;
        }
        
        for (String sessionId : sessions) {
            messaging.convertAndSendToUser(sessionId, "/execution/queue/append", itemQueue);
        }
    }
    
    public void sendProgress(String email, ExecutionQueueDTO itemQueue) {
        
        List<String> sessions = loggedUsers.get(email);

        if (sessions == null) {
            return;
        }
        
        for (String sessionId : sessions) {
            messaging.convertAndSendToUser(sessionId,"/execution/queue/progress", itemQueue);
        }
    }
    
    public void setStatus(ExecutionQueueDTO itemQueue, String status, String... args) {
        itemQueue.setStatus(messageSource.getMessage(status, args, LocaleContextHolder.getLocale()));
    }
}
