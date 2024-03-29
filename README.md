# Spotter

[![Build Status](https://travis-ci.org/sdorra/spotter.svg?branch=master)](https://travis-ci.org/sdorra/spotter)
[![Maven Central](https://img.shields.io/maven-central/v/com.cloudogu.spotter/spotter.svg)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22spotter%22)
[![Quality Gates](https://sonarcloud.io/api/project_badges/measure?project=com.cloudogu.spotter%3Aspotter&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.cloudogu.spotter%3Aspotter)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.cloudogu.spotter%3Aspotter&metric=coverage)](https://sonarcloud.io/dashboard?id=com.cloudogu.spotter%3Aspotter)

Spotter is a library which is able to detect the content type of a file. 
It is also able to detect the programming language of source files. 
Spotter uses [Apache Tika](https://tika.apache.org/) for the content type recognition and uses the [language.yml](https://raw.githubusercontent.com/github/linguist/master/lib/linguist/languages.yml) from the [Linguist project](https://github.com/github/linguist).

## Usage

Add the latest stable version of to the dependency management tool of your choice.

E.g. for maven:

```xml
<dependency>
    <groupId>com.cloudogu.spotter</groupId>
    <artifactId>spotter-core</artifactId>
    <version>x.y.z</version>
</dependency>
```

Use the latest version from maven central: [![Maven Central](https://img.shields.io/maven-central/v/com.cloudogu.spotter/spotter.svg)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22spotter%22)

For versions before 4.0.0 use the following coordinates:

```xml
<dependency>
    <groupId>com.github.sdorra</groupId>
    <artifactId>spotter-core</artifactId>
    <version>x.y.z</version>
</dependency>
```

### Examples

Detect the content type and the programming language of a file:

```java
ContentType contentType = ContentTypes.detect("com/cloudogu/spotter/Language.java");
System.out.println("Content-Type: " + contentType);

Optional<Language> language = contentType.getLanguage();
if (language.isPresent()) {
    System.out.println("Language    : " + language.get().getName());
}
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
