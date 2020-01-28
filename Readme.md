# javaAutoStart [![Build Status](https://travis-ci.org/vatbub/javaAutoStart.svg?branch=master)](https://travis-ci.org/vatbub/javaAutoStart) [![Maven Central](https://img.shields.io/maven-central/v/com.github.vatbub/javaAutoStart)](http://search.maven.org/search?q=g:com.github.vatbub%20AND%20a:javaAutoStart)
A simple library to add Java/Kotlin.JVM apps to the autostart of Windows.

## Download
The library is available on ![Maven Central](https://img.shields.io/maven-central/v/com.github.vatbub/javaAutoStart) and on JCenter.
```xml
<dependency>
    <groupId>com.github.vatbub</groupId>
    <artifactId>javaAutoStart</artifactId>
    <version>1.1</version>
</dependency>
```

Latest version: [![Maven Central](https://img.shields.io/maven-central/v/com.github.vatbub/javaAutoStart?label=javaAutoStart)](http://search.maven.org/search?q=g:com.github.vatbub%20AND%20a:javaAutoStart)

If you get some strange JNA exceptions, also add these dependencies:

```xml
<dependencies>
    ...
    <dependency>
        <groupId>net.java.dev.jna</groupId>
        <artifactId>jna</artifactId>
        <version>5.5.0</version>
    </dependency>
    <dependency>
        <groupId>net.java.dev.jna</groupId>
        <artifactId>jna-platform</artifactId>
        <version>5.5.0</version>
    </dependency>
    ...
</dependencies>
```

The latest versions of the above dependencies are: [![Maven Central](https://img.shields.io/maven-central/v/net.java.dev.jna/jna?label=jna)](http://search.maven.org/search?q=g:net.java.dev.jna%20AND%20a:jna) [![Maven Central](https://img.shields.io/maven-central/v/net.java.dev.jna/jna-platform?label=jna-platform)](http://search.maven.org/search?q=g:net.java.dev.jna%20AND%20a:jna-platform)

## Usage
### Java
```java
import com.github.vatbub.javaautostart.AutoStartManager;

public class JavaDemo {
    private String appId = "com.github.vatbub.demoApp";

    public void autoStartDemo(){
        AutoStartManager manager = new AutoStartManager(appId);

        boolean isInAutoStart = manager.isInAutoStart();

        if (!isInAutoStart)
            manager.addToAutoStart(/* optional: additional launch arguments */);

        manager.removeFromAutoStart();
    }
}
```

### Kotlin
```kotlin
import com.github.vatbub.javaautostart.AutoStartManager

private val appId = "com.github.vatbub.demoApp"

fun autoStartDemo() {
    val manager = AutoStartManager(appId)
    
    val isInAutoStart = manager.isInAutoStart
    if (!isInAutoStart)
        manager.addToAutoStart(/* optional: additional launch arguments */)
    
    manager.removeFromAutoStart()
}
```

## License
This library is licensed under the Apache License v2.
Open the `LICENSE.txt` file of this repository for more info.

## Contributing
Contributions are welcome.
As always, you can submit an issue, fork and submit PRs.
