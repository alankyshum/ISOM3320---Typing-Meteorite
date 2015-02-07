# Typing Meteorite - README
`Git repo imported from Bitbucket.`
> ISOM3320 - Spring, 2015, by Teddy Lun, Hona Dopa, Jack Hui, and me.

# Prerequisites
1. **JDK 1.8+** for the support of *Lambda Expression*. It is assumed that **JRE 1.8+** has also been installed alongside.
1. (*Optional*) A markdown viewer to view this README file in full experience.
  * Online Viewer: [Dillinger](http://dillinger.io/)
  * Offline Viewer: Atom with its builtin markdown viewer

# To Build
```bash
cd /SOURCE_codes
javac -d . *.java
jar cvfm meteorite.jar manifest.mf meteorite db
```
# To Run
```bash
java -jar meteorite.jar
```

---
# Toubleshoot
## Java Security Settings
For Java 8+, you need to add the directory into the exception list, or local `.jar` won't run.
  * (e.g.) `file:///D:/test/__artifacts_temp/`
    * Not backslash `\` but slash `/`
    * Begin file `file:///`
    * Should be a **directory path**, not a file

## Honor Board not saved
This is caused by having missed the `db` directory in where the .jar file being located. Please follow the **step 3** in **To Build** section.
