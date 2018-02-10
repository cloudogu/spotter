# Spotter

Spotter is a library which is able to detect the content type of a file. 
It is also able to detect the programming language of source files. 
Spotter uses [Apache Tika](https://tika.apache.org/) for the content type recognition and uses the [language.yml](https://raw.githubusercontent.com/github/linguist/master/lib/linguist/languages.yml) from the [Linguist project](https://github.com/github/linguist).

## Usage

```java
ContentType contentType = ContentTypes.detect("com/github/sdorra/spotter/Language.java");
System.out.println("Content-Type: " + contentType);

Optional<Language> language = contentType.getLanguage();
if (language.isPresent()) {
    System.out.println("Language    : " + language.get().getName());
}
```
