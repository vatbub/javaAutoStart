# javaAutoStart
A simple library to add Java/Kotlin.JVM apps to the autostart of Windows.

## Download
Coming soon, hang tight. For now:
1. Clone the repo
2. `cd` into the repo and run `mvn install` to compile the lib and install it to your local Maven repository.
3. Then, add the following to your dependencies:

```xml
<dependency>
    <groupId>com.github.vatbub</groupId>
    <artifactId>javaAutoStart</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

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
