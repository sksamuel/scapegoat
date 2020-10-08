Scapegoat
=========

[![Codecov](https://img.shields.io/codecov/c/github/sksamuel/scapegoat)](https://codecov.io/gh/sksamuel/scapegoat)
[<img src="https://img.shields.io/maven-central/v/com.sksamuel.scapegoat/scalac-scapegoat-plugin_2.11.12.svg?label=latest%20release%20for%202.11.12"/>](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22scalac-scapegoat-plugin_2.11.12%22)
[<img src="https://img.shields.io/maven-central/v/com.sksamuel.scapegoat/scalac-scapegoat-plugin_2.12.11.svg?label=latest%20release%20for%202.12.11"/>](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22scalac-scapegoat-plugin_2.12.11%22)
[<img src="https://img.shields.io/maven-central/v/com.sksamuel.scapegoat/scalac-scapegoat-plugin_2.13.3.svg?label=latest%20release%20for%202.13.3"/>](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22scalac-scapegoat-plugin_2.13.3%22)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

Scapegoat is a Scala static code analyzer, what is more colloquially known as a code lint tool or linter. Scapegoat works in a similar vein to Java's [FindBugs](http://findbugs.sourceforge.net/) or [checkstyle](http://checkstyle.sourceforge.net/), or Scala's [Scalastyle](https://github.com/scalastyle/scalastyle).

A static code analyzer is a tool that flag suspicious language usage in code. This can include behavior likely to lead or bugs, non idiomatic usage of a language, or just code that doesn't conform to specified style guidelines.

**What's the difference between this project and Scalastyle (or others)?**

Scalastyle is a similar linting tool which focuses mostly on enforcing style/code standards. There's no problems running multiple analysis tools on the same codebase. In fact it could be beneficial as the total set of possible warnings is the union of the inspections of all the enabled tools. The worst case is that the same warnings might be generated by multiple tools.

## Usage
Scapegoat is developed as a scala compiler plugin, which can then be used inside your build tool.

### SBT
See: [sbt-scapegoat](https://github.com/sksamuel/sbt-scapegoat) for SBT integration.

### Maven

Firstly you need to add scapegoat plugin as a dependency:

```xml
<dependency>
    <groupId>com.sksamuel.scapegoat</groupId>
    <artifactId>scalac-scapegoat-plugin_${scala.version}</artifactId>
    <version>1.3.12</version>
</dependency>
```

Then configure `scala-maven-plugin` by adding `compilerPlugin`

```xml
<plugin>
    <groupId>net.alchim31.maven</groupId>
    <artifactId>scala-maven-plugin</artifactId>
    <configuration>
        <args>
            <arg>-P:scapegoat:dataDir:./target/scapegoat</arg>
        </args>
        <compilerPlugins>
            <compilerPlugin>
                <groupId>com.sksamuel.scapegoat</groupId>
                <artifactId>scalac-scapegoat-plugin_${scala.binary.version}</artifactId>
                <version>1.3.3</version>
            </compilerPlugin>
        </compilerPlugins>
    </configuration>
</plugin>
```
The only required parameter is `dataDir` (where report will be generated):

`<arg>-P:scapegoat:dataDir:./target/scapegoat</arg>`

You can pass other configuration flags same way, e.g.

`<arg>-P:scapegoat:disabledInspections:FinalModifierOnCaseClass</arg>`

Note: You may use a separate maven profile, so that the dependency doesn't go to you runtime classpath.

### Gradle with a plugin

Use [gradle-scapegoat-plugin by @eugene-sy](https://github.com/eugene-sy/gradle-scapegoat-plugin)

### Gradle - manually

Firstly you need to add scapegoat plugin as a dependency:

```groovy
dependencies {
  compile 'com.sksamuel.scapegoat:scalac-scapegoat-plugin_2.12:1.3.3'
  scalaCompilerPlugin "com.sksamuel.scapegoat:scalac-scapegoat-plugin_2.12:1.3.3"
}

```

Then configure `scala-compiler-plugin` 

```groovy
configurations {
  scalaCompilerPlugin
}

tasks.withType(ScalaCompile) {
  scalaCompileOptions.additionalParameters = [
    "-Xplugin:" + configurations.scalaCompilerPlugin.asPath,
    "-P:scapegoat:dataDir:" + buildDir + "/scapegoat"
  ]
}

```

The only required parameter is `dataDir` (where report will be generated):

`"-P:scapegoat:dataDir:" + buildDir + "/scapegoat",`

You can pass other configuration flags adding it to the `additionalParameters` list, e.g.

`"-P:scapegoat:disabledInspections:FinalModifierOnCaseClass"`

## Full list of compiler flags

| Flag | Parameters | Required |
|------|------------|----------|
|`-P:scapegoat:dataDir:`|Path to reports directory for the plugin.|true|
|`-P:scapegoat:disabledInspections:`|Colon separated list of disabled inspections (defaults to none).|false|
|`-P:scapegoat:enabledInspections:`|Colon separated list of enabled inspections (defaults to all).|false|
|`-P:scapegoat:customInspectors:`|Colon separated list of custom inspections.|false|
|`-P:scapegoat:ignoredFiles:`|Colon separated list of regexes to match files to ignore.|false|
|`-P:scapegoat:verbose:`|Boolean flag that enables/disables verbose console messages.|false|
|`-P:scapegoat:consoleOutput:`|Boolean flag that enables/disables console report output.|false|
|`-P:scapegoat:reports:`|Colon separated list of reports to generate. Valid options are `none`, `xml`, `html`, `scalastyle`, `markdown`, or `all`.|false|
|`-P:scapegoat:overrideLevels:`|Overrides the built in warning levels. Should be a colon separated list of `name=level` expressions.|false|
|`-P:scapegoat:sourcePrefix:`|Overrides source prefix if it differs from `src/main/scala`, for ex. `app/` for Play applications.|false|
|`-P:scapegoat:minimalLevel:`|Provides minimal level of inspection displayed in reports and in the console.|false|

## Reports

Here is sample output from the console during the build for a project with warnings/errors:

```
[warning] [scapegoat] Unused method parameter - org.ensime.util.ClassIterator.scala:46
[warning] [scapegoat] Unused method parameter - org.ensime.util.ClassIterator.scala:137
[warning] [scapegoat] Use of var - org.ensime.util.ClassIterator.scala:22
[warning] [scapegoat] Use of var - org.ensime.util.ClassIterator.scala:157
[   info] [scapegoat]: Inspecting compilation unit [FileUtil.scala]
[warning] [scapegoat] Empty if statement - org.ensime.util.FileUtil.scala:157
[warning] [scapegoat] Expression as statement - org.ensime.util.FileUtil.scala:180

```

And if you prefer a prettier report, here is a screen shot of the type of HTML report scapegoat generates:

![screenshot](https://raw.githubusercontent.com/sksamuel/scapegoat/master/screenshot1.png)

## Configuration

For instructions on suppressing warnings by file, by inspection or by line see [the sbt-scapegoat README](https://github.com/sksamuel/sbt-scapegoat).

### Inspections

There are currently 118 inspections. An overview list is given, followed by a more detailed description of each inspection after the list (todo: finish rest of detailed descriptions)

| Name | Brief Description | Default Level |
|------|-------------------|---------------|
| ArrayEquals | Checks for comparison of arrays using `==` which will always return false | Info |
| ArraysInFormat | Checks for arrays passed to String.format | Error |
| ArraysToString | Checks for explicit toString calls on arrays | Warning |
| AsInstanceOf | Checks for use of `asInstanceOf` | Warning |
| AvoidOperatorOverload | Checks for mental symbolic method names | Info |
| AvoidSizeEqualsZero | Traversable.size can be slow for some data structure, prefer .isEmpty | Warning |
| AvoidSizeNotEqualsZero | Traversable.size can be slow for some data structure, prefer .nonEmpty | Warning |
| AvoidToMinusOne | Checks for loops that use `x to n-1` instead of `x until n` | Info |
| BigDecimalDoubleConstructor | Checks for use of `BigDecimal(double)` which can be unsafe | Warning |
| BigDecimalScaleWithoutRoundingMode | `setScale()` on a `BigDecimal` without setting the rounding mode can throw an exception | Warning |
| BoundedByFinalType | Looks for types with upper bounds of a final type | Warning |
| BrokenOddness | checks for a % 2 == 1 for oddness because this fails on negative numbers | Warning |
| CatchException | Checks for try blocks that catch Exception | Warning |
| CatchFatal | Checks for try blocks that catch fatal exceptions: VirtualMachineError, ThreadDeath, InterruptedException, LinkageError, ControlThrowable | Warning |
| CatchNpe | Checks for try blocks that catch null pointer exceptions | Error |
| CatchThrowable | Checks for try blocks that catch Throwable | Warning |
| ClassNames | Ensures class names adhere to the style guidelines | Info |
| CollectionIndexOnNonIndexedSeq | Checks for indexing on a Seq which is not an IndexedSeq | Warning |
| CollectionNamingConfusion | Checks for variables that are confusingly named | Info |
| CollectionNegativeIndex | Checks for negative access on a sequence eg `list.get(-1)` | Warning |
| CollectionPromotionToAny | Checks for collection operations that promote the collection to `Any` | Warning |
| ComparingFloatingPointTypes | Checks for equality checks on floating point types | Error |
| ComparingUnrelatedTypes | Checks for equality comparisons that cannot succeed | Error |
| ComparisonToEmptyList | Checks for code like `a == List()` or `a == Nil` | Info |
| ComparisonToEmptySet | Checks for code like `a == Set()` or `a == Set.empty` | Info |
| ComparisonWithSelf | Checks for equality checks with itself | Warning |
| ConstantIf | Checks for code where the if condition compiles to a constant | Warning |
| DivideByOne | Checks for divide by one, which always returns the original value | Warning |
| DoubleNegation | Checks for code like `!(!b)` | Info |
| DuplicateImport | Checks for import statements that import the same selector | Info |
| DuplicateMapKey | Checks for duplicate key names in Map literals | Warning |
| DuplicateSetValue | Checks for duplicate values in set literals | Warning |
| EitherGet | Checks for use of .get on Left or Right | Error |
| EmptyCaseClass | Checks for case classes like `case class Faceman()` | Info |
| EmptyFor | Checks for empty `for` loops | Warning |
| EmptyIfBlock | Checks for empty `if` blocks | Warning |
| EmptyInterpolatedString | Looks for interpolated strings that have no arguments | Error |
| EmptyMethod | Looks for empty methods | Warning |
| EmptySynchronizedBlock | Looks for empty synchronized blocks | Warning |
| EmptyTryBlock | Looks for empty try blocks | Warning |
| EmptyWhileBlock | Looks for empty while loops | Warning |
| ExistsSimplifiableToContains | `exists(x => x == b)` replaceable with `contains(b)` | Info |
| FilterDotHead | `.filter(x => ).head` can be replaced with `find(x => ) match { .. } ` | Info |
| FilterDotHeadOption | `.filter(x =>).headOption` can be replaced with `find(x => )` | Info |
| FilterDotIsEmpty | `.filter(x => ).isEmpty` can be replaced with `!exists(x => )` | Info |
| FilterDotSize | `.filter(x => ).size` can be replaced more concisely with with `count(x => )` | Info |
| FilterOptionAndGet | `.filter(_.isDefined).map(_.get)` can be replaced with `flatten` | Info |
| FinalModifierOnCaseClass | Using Case classes without `final` modifier can lead to surprising breakage | Info |
| FinalizerWithoutSuper | Checks for overridden finalizers that do not call super | Warning |
| FindAndNotEqualsNoneReplaceWithExists | `.find(x => ) != None` can be replaced with `exist(x => )` | Info |
| FindDotIsDefined | `find(x => ).isDefined` can be replaced with `exist(x => )` | Info |
| IllegalFormatString | Looks for invalid format strings | Error |
| ImpossibleOptionSizeCondition | Checks for code like `option.size > 2` which can never be true | Error |
| IncorrectNumberOfArgsToFormat | Checks for wrong number of arguments to `String.format` | Error |
| IncorrectlyNamedExceptions | Checks for exceptions that are not called *Exception and vice versa | Error |
| InvalidRegex | Checks for invalid regex literals | Info |
| IsInstanceOf | Checks for use of `isInstanceOf` | Warning |
| JavaConversionsUse | Checks for use of implicit Java conversions | Warning |
| ListAppend | Checks for List :+ which is O(n) | Info |
| ListSize | Checks for `List.size` which is O(n). | Info |
| LonelySealedTrait | Checks for sealed traits which have no implementation | Error |
| LooksLikeInterpolatedString | Finds strings that look like they should be interpolated but are not | Warning |
| MapGetAndGetOrElse | `Map.get(key).getOrElse(value)` can be replaced with `Map.getOrElse(key, value)` | Error |
| MaxParameters | Checks for methods that have over 10 parameters | Info |
| MethodNames | Warns on method names that don't adhere to the Scala style guidelines | Info |
| MethodReturningAny | Checks for defs that are defined or inferred to return `Any` | Warning |
| ModOne | Checks for `x % 1` which will always return `0` | Warning |
| NanComparison | Checks for `x == Double.NaN` which will always fail | Error |
| NegationIsEmpty | `!Traversable.isEmpty` can be replaced with `Traversable.nonEmpty` | Info |
| NegationNonEmpty | `!Traversable.nonEmpty` can be replaced with `Traversable.isEmpty` | Info |
| NoOpOverride | Checks for code that overrides parent method but simply calls super | Info |
| NullAssignment | Checks for use of `null` in assignments | Warning |
| NullParameter | Checks for use of `null` in method invocation | Warning |
| ObjectNames | Ensures object names adhere to the Scala style guidelines | Info |
| OptionGet | Checks for `Option.get` | Error |
| OptionSize | Checks for `Option.size` | Error |
| ParameterlessMethodReturnsUnit | Checks for `def foo : Unit` | Warning |
| PartialFunctionInsteadOfMatch | Warns when you could use a partial function directly instead of a match block | Info |
| PointlessTypeBounds | Finds type bounds of the form `[A <: Any]` or `[A >: Nothing]`| Warning |
| PreferMapEmpty | Checks for Map() when could use Map.empty | Info |
| PreferSeqEmpty | Checks for Seq() when could use Seq.empty | Info |
| PreferSetEmpty | Checks for Set() when could use Set.empty | Info |
| ProductWithSerializableInferred | Checks for vals that have `Product with Serializable` as their inferred type | Warning |
| PublicFinalizer | Checks for overridden finalizes that are public | Info |
| RedundantFinalModifierOnMethod | Redundant `final` modifier on method that cannot be overridden | Info |
| RedundantFinalModifierOnVar | Redundant `final` modifier on var that cannot be overridden | Info |
| RedundantFinalizer | Checks for empty finalizers. | Warning |
| RepeatedCaseBody | Checks for case statements which have the same body | Warning |
| RepeatedIfElseBody | Checks for the main branch and the else branch of an `if` being the same | Warning |
| ReverseFunc | `reverse` followed by `head`, `headOption`, `iterator`, or`map` can be replaced, respectively, with `last`, `lastOption`, `reverseIterator`, or `reverseMap` | Info |
| ReverseTailReverse | `.reverse.tail.reverse` can be replaced with `init` | Info |
| ReverseTakeReverse | `.reverse.take(...).reverse` can be replaced with `takeRight` | Info |
| SimplifyBooleanExpression | `b == false` can be simplified to `!b` | Info |
| StripMarginOnRegex | Checks for .stripMargin on regex strings that contain a pipe | Error |
| SubstringZero | Checks for `String.substring(0)` | Info |
| SuspiciousMatchOnClassObject | Finds code where matching is taking place on class literals | Warning |
| SwallowedException | Finds catch blocks that don't handle caught exceptions | Warning |
| SwapSortFilter | `sort.filter` can be replaced with `filter.sort` for performance | Info |
| TryGet | Checks for use of `Try.get` | Error |
| TypeShadowing | Checks for shadowed type parameters in methods | Warning |
| UnnecessaryConversion | Checks for unnecessary `toInt` on instances of Int or `toString` on Strings, etc. | Warning |
| UnnecessaryIf | Checks for code like `if (expr) true else false` | Info |
| UnnecessaryReturnUse | Checks for use of `return` keyword in blocks | Info |
| UnreachableCatch | Checks for catch clauses that cannot be reached | Warning |
| UnsafeContains | Checks for `List.contains(value)` for invalid types | Error |
| UnsafeStringContains | Checks for `String.contains(value)` for invalid types | Error |
| UnsafeTraversableMethods | Check unsafe traversable method usages (head, tail, init, last, reduce, reduceLeft, reduceRight, max, maxBy, min, minBy) | Error |
| UnusedMethodParameter | Checks for unused method parameters | Warning |
| UseCbrt | Checks for use of `math.pow` for calculating `math.cbrt` | Info |
| UseExpM1 | Checks for use of `math.exp(x) - 1` instead of `math.expm1(x)` | Info |
| UseLog10 | Checks for use of `math.log(x)/math.log(10)` instead of `math.log10(x)` | Info |
| UseLog1P | Checks for use of `math.log(x + 1)` instead of `math.log1p(x)` | Info |
| UseSqrt | Checks for use of `math.pow` for calculating `math.sqrt` | Info |
| VarClosure | Finds closures that reference var | Warning |
| VarCouldBeVal | Checks for `var`s that could be declared as `val`s | Warning |
| VariableShadowing | Checks for multiple uses of the variable name in nested scopes | Warning |
| WhileTrue | Checks for code that uses a `while(true)` or `do { } while(true)` block. | Warning |
| ZeroNumerator | Checks for dividing by 0 by a number, eg `0 / x` which will always return `0` | Warning |

##### Arrays to string

Checks for explicit toString calls on arrays. Since toString on an array does not perform a deep toString, like say scala's List, this is usually a mistake.

##### CollectionIndexOnNonIndexedSeq

Checks for calls of `.apply(idx)` on a `Seq` where the index is not a literal and the `Seq` is not an `IndexedSeq`.

*Rationale* If code which expects O(1) positional access to a Seq is given a non-IndexedSeq (such as a List, where indexing is O(n)) then this may cause poor performance.

##### ComparingUnrelatedTypes

Checks for equality comparisons that cannot succeed because the types are unrelated. Eg `"string" == BigDecimal(1.0)`. The scala compiler has a less strict version of this inspection.

##### ConstantIf

Checks for if statements where the condition is always true or false. Not only checks for the boolean literals, but also any expression that the compiler is able to turn into a constant value. Eg, `if (0 < 1) then else that`

##### IllegalFormatString

Checks for a format string that is not invalid, such as invalid conversions, invalid flags, etc. Eg, `"% s"`, `"%qs"`, `%.-4f"`

##### IncorrectNumberOfArgsToFormat

Checks for an incorrect number of arguments to String.format. Eg, `"%s %s %f".format("need", "three")` flags an error because the format string specifies 3 parameters but the call only provides 2.

##### InvalidRegex

Checks for invalid regex literals that would fail at compile time. Either dangling metacharacters, or unclosed escape characters, etc that kind of thing.

##### List size

Checks for .size on an instance of List. Eg, `val a = List(1,2,3); a.size`

*Rationale*: List.size is O(n) so for performance reasons if .size is needed on a list that could be large, consider using an alternative with O(1), eg Array, Vector or ListBuffer.

##### Redundant finalizer

Checks for empty finalizers. This is redundant code and should be removed. Eg, `override def finalize : Unit = { }`

##### PreferSetEmpty

Indicates where code using Set() could be replaced with Set.empty. Set() instantiates a new instance each time it is invoked, whereas Set.empty returns a pre-instantiated instance.

##### UnnecessaryReturnUse

Checks for use of return in a function or method. Since the final expression of a block is always the return value, using return is unnecessary. Eg, `def foo = { println("hello"); return 12; }`

##### UnreachableCatch

Checks for catch clauses that cannot be reached. This means the exception is dead and if you want that exception to take precedence you should move up further up the case list.

##### UnsafeContains

Checks for `List.contains(value)` for invalid types. The method for contains accepts any types. This inspection finds situations when you have a list of type A and you are checking for contains on type B which cannot hold.

##### While true

Checks for code that uses a `while(true)` or `do { } while(true)` block.

*Rationale*: This type of code is usually not meant for production as it will not return normally. If you need to loop until interrupted then consider using a flag.

### Suppressing Warnings by Method or Class
You can suppress a specific warning by method or by class using the java.lang.SuppressWarnings anotation.

Use the simple name of the inspection to be ignored as the argument, or use "all" to suppress all scapegoat warnings in the specified scope.

Some examples:

```scala
@SuppressWarnings(Array("all"))
class Test {
  def hello : Unit = {
    val s : Any = "sammy"
    println(s.asInstanceOf[String])
  }
} 

class Test2 {
  @SuppressWarnings(Array("AsInstanceOf"))
  def hello : Unit = {
    val s : Any = "sammy"
    println(s.asInstanceOf[String])
  }
}
```


## Other static analysis tools:

* ScalaStyle (Scala) - https://github.com/scalastyle/scalastyle/wiki
* Linter (Scala) - https://github.com/HairyFotr/linter
* WartRemover (Scala) - https://github.com/puffnfresh/wartremover
* Findbugs (JVM) - http://findbugs.sourceforge.net/bugDescriptions.html
* Fb-contrib (JVM) - http://fb-contrib.sourceforge.net/
* CheckStyle (Java) - http://checkstyle.sourceforge.net/availablechecks.html
* PMD (Java) - http://pmd.sourceforge.net/pmd-5.0.3/rules/index.html
* Error-prone (Java) - https://github.com/google/error-prone
* CodeNarc (Groovy) - http://codenarc.sourceforge.net/codenarc-rule-index.html
* PVS-Studio (C++) - http://www.viva64.com/en/d/
* Coverity (C++) - http://www.slideshare.net/Coverity/static-analysis-primer-22874326 (6,7)
* CppCheck (C++) - http://cppcheck.sourceforge.net/
* OCLint (C++/ObjC) - http://docs.oclint.org/en/dev/rules/index.html
* JSHint (Javascript) - http://jshint.com/
* JavascriptLint (Javascript) - http://www.javascriptlint.com/
* ClosureLinter (Javascript) - https://developers.google.com/closure/utilities/
