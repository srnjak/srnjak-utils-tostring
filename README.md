# srnjak-utils-tostring

Utility for generating a string representation of an object. It extends
[Apache Commons Lang 3](https://commons.apache.org/proper/commons-lang/)
with getter based output, an exclusion annotation, and a recursion style you
can aim at the types you care about.

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

## Requirements

Java 11 or newer.

## Installation

```xml
<dependency>
    <groupId>com.srnjak</groupId>
    <artifactId>srnjak-utils-tostring</artifactId>
    <version>1.1.0</version>
</dependency>
```

`commons-lang3` is declared with `provided` scope and therefore does **not**
come along transitively. Declare it yourself:

```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.20.0</version>
</dependency>
```

## Usage

The examples use this object:

```java
Person person = new Person("Jane", 30, new Address("Main Street 1", "Springfield"), Color.RED);
```

### From fields

```java
ToStringByFieldsBuilder.toString(person);
// com.example.Person@13221655[address=com.example.Address@448139f0,age=30,favorite=RED,name=Jane]
```

Transient and static fields are left out.

### From getters

```java
ToStringByGettersBuilder.toString(person, ToStringStyle.NO_CLASS_NAME_STYLE);
// [address=com.example.Address@448139f0,age=30,favorite=RED,name=Jane]
```

Properties are read through `java.beans.Introspector`, so computed
properties that have no backing field are included too.

Both builders order members alphabetically by name, so output from one can
be compared against the other. For a type whose properties all have backing
fields, the two produce the same string.

Any `ToStringStyle` from Commons Lang works with either builder.

### Excluding members

Annotate a field for the field based builder, or a getter for the getter
based one:

```java
public class Account {

    private final String owner = "Jane";

    @ToStringExclude
    private final String secret = "s3cret";
}
```

```java
ToStringByFieldsBuilder.toString(account, ToStringStyle.NO_CLASS_NAME_STYLE);
// [owner=Jane]
```

`ToStringByGettersBuilder` additionally honours Commons Lang's own
`org.apache.commons.lang3.builder.ToStringExclude` when it is placed on the
backing field.

### Values that cannot be read

If reading a value throws a `RuntimeException`, it is logged at `FINER` and
reported as `<N/A>` instead of failing the whole call:

```java
ToStringByGettersBuilder.toString(broken, ToStringStyle.NO_CLASS_NAME_STYLE);
// [<N/A>,fine=ok]
```

Note that the member name is not part of that entry.

### Recursion

By default a nested object is rendered by its own `toString()`. `RecursiveStyle`
lets you pick which types get expanded instead, by annotation, by class or by
package prefix:

```java
ToStringStyle style = RecursiveStyle.builder()
        .toStringBuilder(ToStringByFieldsBuilder.class)
        .acceptPackages("com.example")
        .build();

ToStringByFieldsBuilder.toString(person, style);
// com.example.Person@13221655[
//   address=com.example.Address@448139f0[city=Springfield,street=Main Street 1],
//   age=30,favorite=RED,name=Jane]
```

> **Always call `toStringBuilder(...)`.**
> It defaults to Commons Lang's `ToStringBuilder`, which has no static
> `toString(Object, ToStringStyle)` for the style to call back into. Leaving
> the default does not fail loudly — every accepted object silently renders as
> `<N/A>`. Pass `ToStringByFieldsBuilder.class` or
> `ToStringByGettersBuilder.class`.

Enums are never expanded, whatever the filters say, so they keep rendering as
their constant name.

Maps are rendered entry by entry rather than through `Map.toString()`, with
`<null>` for absent values:

```java
// com.example.Holder@4c98385c[entries=java.util.LinkedHashMap@5fcfe4b2{a=1,b=<null>}]
```

## Upgrading to 1.1.0

`ToStringByGettersBuilder.toString(Object)` and
`toString(Object, ToStringStyle)` used to render **fields**, because they
resolved to an inherited static method rather than building a getter based
builder. They now render properties, as the class name always promised.

Output therefore changes for existing callers of those two methods: computed
properties appear, `@ToStringExclude` on a getter starts being honoured, and
the inherited `class=...` entry no longer leaks into the result.

`ToStringByFieldsBuilder` now orders fields alphabetically rather than by
declaration, so that both builders agree. `Class.getDeclaredFields()`
guarantees no particular order in the first place, so the previous output was
only incidentally stable.

## Snapshots

Development builds are published to the Central Portal snapshot repository:

```xml
<repositories>
    <repository>
        <id>central-snapshots</id>
        <url>https://central.sonatype.com/repository/maven-snapshots/</url>
        <releases><enabled>false</enabled></releases>
        <snapshots><enabled>true</enabled></snapshots>
    </repository>
</repositories>
```

Snapshots are removed after 90 days.

## Building

```bash
mvn clean package
```

## License

[Apache License 2.0](LICENSE)
