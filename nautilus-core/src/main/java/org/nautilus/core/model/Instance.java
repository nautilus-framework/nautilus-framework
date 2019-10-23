package org.nautilus.core.model;

import java.util.List;

import org.nautilus.core.gui.Tab;

public abstract class Instance {
    
    public abstract List<Tab> getTabs(Instance data);
}
