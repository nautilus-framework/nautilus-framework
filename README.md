<img src="https://user-images.githubusercontent.com/114015/101218438-135c5080-3651-11eb-9191-ce6ff0be2d4f.png" width="400px">

A preference-base objective reduction for SBSE problems

[![GitHub Release](https://img.shields.io/github/release/nautilus-framework/nautilus-framework.svg)](https://github.com/nautilus-framework/nautilus-framework/releases/latest)
[![GitHub contributors](https://img.shields.io/github/contributors/nautilus-framework/nautilus-framework.svg)](https://github.com/nautilus-framework/nautilus-framework/graphs/contributors)
[![GitHub stars](https://img.shields.io/github/stars/nautilus-framework/nautilus-framework.svg)](https://github.com/almende/nautilus-framework/nautilus-framework)
[![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://opensource.org/licenses/mit-license.php)
[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://github.com/ellerbrock/open-source-badges/)

The original paper was published at:

> T. Ferreira, S. R. Vergilio and M. Kessentini, "Nautilus: An Interactive Plug and Play Search Based Software Engineering Framework," in IEEE Software, doi: 10.1109/MS.2020.3039694.

## Usage

To use this tool, you have to following the steps described as follows.

#### 1. Requirements

You need to download MongoDB in your machine (available at https://www.mongodb.com), or you can use https://www.mongodb.com/cloud/atlas

#### 2. Parameter Settings

Once you have installed MongoDB, you need to set the connection url. To this end, please open the  ```application.properties``` file at:

```java
nautilus-web/src/main/resources/
```

and, find and change ```spring.data.mongodb.uri``` values to the provided connection url. The default url is ```mongodb://localhost:27017/nautilus```

#### 3. Run

Now, the last step is to run the main class. This one is located at:

```java
nautilus-web/src/main/java/org/nautilus/web/Launcher.java
```

Done. Now, access on your browser ```http://localhost:8081``` to have access to the system. There is a default user and the information about it is located in the  ```application.properties``` file.

## Screenshot

The following image shows an execution considering all objective functions

<kbd>
  <img src="https://user-images.githubusercontent.com/114015/101218578-51f20b00-3651-11eb-8bc7-39cad9b448a0.png">
</kbd>

## Questions or Suggestions

Feel free to create <a href="https://github.com/nautilus-framework/nautilus-framework/issues">issues</a> here as you need

## Contribute

Contributions to the this project are very welcome! We can't do this alone! Feel free to fork this project, work on it and then make a pull request.

## Authors

* **Thiago Ferreira** - *Initial work*

See also the list of [contributors](https://github.com/nautilus-framework/nautilus-framework/graphs/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
