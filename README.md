## Nand2Tetris
This is Project 6 of the course Build a Modern Computer from First Principles: From Nand to Tetris. <br>
### Developing a Hack Assembler
Develop an assembler that translates Hack assembly programs into executable Hack binary code
- Parser: unpacks each instruction into its underlying fields
- Code: translates each field into its corresponding binary value
- SymbolTable: manages the symbol table
- HackAssembler: initializes I/O files and drives the process.

Usage
Under the src file:
```bash
javac HackAssembler.java
```

This will create .class file for the project.
Then use the following command to create the Xxx.hack file.
```bash
java HackAssembler Xxx.asm
```