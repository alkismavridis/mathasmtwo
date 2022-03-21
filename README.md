# ABOUT THE PROJECT
MathAsm is my personal project.
It is a parsable language for building mathematical theories.

You define axioms, and then use a set of "operations" to combine them into theorems. The operations are structure in a way that guarantees that theorems follow from the axioms.

In the series, I will live-code the whole creation of that project. We will be creating the rules of MathAsm, and coding a parser that can read MathAsm and "compile" mathematical theories, reading their axioms and "calculating" their theorems. We will be investigating the rules of that parser, aka writing an software application that "implements" MathAsm.

This could take a form of a web application with a UI where the user can "read" his/her theories, or a CLI tool with direct access to the filesystem, or even both.


# Run tests:
`./gradlew test`

# Run with:
`./gradlew bootRun`

# Build a JAR for production with:
`./gradlew build -x test`
