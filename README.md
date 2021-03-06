# LibLoader
[![](https://jitpack.io/v/Phyrone/LibLoader.svg)](https://jitpack.io/#Phyrone/LibLoader) [![Build Status](https://travis-ci.org/Phyrone/LibLoader.svg?branch=master)](https://travis-ci.org/Phyrone/LibLoader)  

#### Its a Library to import Maven-Dependecys at Runtime
## Usage
### Important!
#### Your ParentClassloader must be an URLClassloader
```java
/* Create Libloader */
LibLoader libLoader = new LibLoader()

/* Resolve Artifact (download if not in .m2 Folder)*/
libLoader.require("<GroupID>:<ArtifactID>:<Version>");

/* add Maven Repository */
libLoader.addRepository("<Your Maven Repository URL>");
```


## Import
### Maven
```xml
<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
</repositories>
```
##### Version: [![](https://jitpack.io/v/Phyrone/LibLoader.svg)](https://jitpack.io/#Phyrone/LibLoader)
```xml
<dependency>
	    <groupId>com.github.Phyrone.LibLoader</groupId>
	    <artifactId>LibLoader-Core</artifactId>
	    <version>VERSION</version>
</dependency>
```
### Gradle
```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
##### Version: [![](https://jitpack.io/v/Phyrone/LibLoader.svg)](https://jitpack.io/#Phyrone/LibLoader)
```gradle
	dependencies {
	        implementation 'com.github.Phyrone.LibLoader:LibLoader-Core:VERSION'
	}
```

##### Thaks to [GiantTree](https://github.com/GiantTreeLP)
