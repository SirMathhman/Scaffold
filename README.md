# Scaffold
---
Scaffold is a package manager designed to be as minimalist as possible. A major problem with modern build systems is that
they are often composed of both a package manager and a build system. Moreover, these build systems only support a language 
or set of languages, i.e. (Java, Scala, Kotlin) or (C and C++). As a result, these systems become inflexible and excessively
complicated when working with multiple languages, using a complex suite of sub-projects, or working with different plugins 
or accessories.

Scaffold aims to resolve these difficulties by placing an emphasis on modularity. This repository solely contains the bare
minimum software required to set up a project. All other support (compilation, packaging, publishing) is supported through
dependencies.
---
## Reasoning
The primary language of Scaffold is Java because of Java's modularity. Interpreted languages, such as JavaScript and Python
are slower than average, and are unsuitable for build systems due to the requirement of high performance. However, lower
level languages, such as C, C++, and Rust, do give higher performance but deal with memory, threading, and other "features"
that may complicate Scaffold. Moreover, having to pay attention to these aspects of programming takes away important
effort that should be directed towards the system itself.

The JVM platform fills this gap with fairly high performance, but also a rich set of libraries available in the runtime.
These libraries deal with common tasks such as logging, but also with processes, and the command line.
---
## Installation
Utilizing Scaffold is fairly simple. 

Execute the JAR under the **Releases** tab will begin the process. The JAR will search
for a file in the execution directory of ```module.json```. This file will be responsible for outlining the project and
sub-projects.

A common layout for ```module.json``` may be:

```json
{
  "group": "com.meti",
  "artifact": "scaffold",
  "version": "1.0",
  "content": [
    "/target/production/scaffold.jar",
    "https://github.com/SirMathhman/Scaffold/releases/download/0.2/README.md",
    {
      "group": "org.apache.commons",
      "artifact": "commons-lang3",
      "version": "3.10"
    }
  ],
  "dependencies": [
    "https://github.com/SirMathhman/Scaffold-Init/releases/download/0.2/module.json"
  ],
  "tasks": [
  ],
  "enter": [
    "java -jar /modules/scaffold-init/default.jar"
  ],
  "children": [
  ],
  "exit": [
  ]
}
```

The attributes with empty arrays are not required.

Then, the console will start, where various tasks are executed.

## Contributing

All contributions are very welcome. Any major change should be requested as an issue.

## Licensing

The MIT license is included in the repository and is also present 
[here](https://raw.githubusercontent.com/SirMathhman/Scaffold/master/LICENSE).