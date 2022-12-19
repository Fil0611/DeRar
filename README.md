# DeRar
DeRar is a simple Java program to brute force the keyword of a RAR file.

If there's any problem regarding the program, please post an issue on the program's GitHub page.
If you have ideas to improve the code, make sure to post a pull request.

## Disclaimer
The DeRar project and its modifications are not affiliated with or endorsed by WinRar or RarLab. The program is an independent work and is not officially supported or endorsed by WinRar or RarLab. Use of the program is at your own risk, and the creators of the program cannot be held responsible for any damages or losses that may result from its use.
Also ask the owner of the .rar archive permission before using this tool.

---

# Features
- The user can select several charsets to generate the combinations with, such as lower case, upper case, accented letters, special characters or digits.
- The user can specify the number of threads to run.
- The progress of each thread gets displayed in a window thanks to a progress bar.

---

# ToDo
- [ ] Optimize the code.
- [ ] Improve the GUI.
- [ ] Translate code, comments and prompts in english.
- [ ] Make the user choose between a dictionary or a brute force attack.
- [ ] Add more charsets for the user to choose from.

---

# How to run
1. Create[^1] a folder and put `derar.java` and `utils.java` inside it.
2. Place the .rar archive in the same folder.
3. Open the Windows command prompt [^2] and type the following commands: 

- `javac -encoding UTF-8 derar.java` -> to compile the program
- `java derar` -> to run the program

[^1]: If you cloned the project from GitHub you'll have a folder named `\DeRar\` with a .rar archive to test the program, the keywords for `test.rar` is "abc".

[^2]: I personally reccomend compiling and running the program from the Windows command prompt because from tests it is a little quicker, but if you prefer you can surely run it from applications like VS Code.

---

# How it works
The program generates every combination of words up to the user preferred length, then it tries every single one by running a cmd command.

## Inputs
The program asks the user to input the length of the word that will get generated with the combination algorithm. This means that the program will combine the selected characters and create words that go from the length of 1 character to the inputted length.

The user will then be asked to write the name of the file (not including the .rar extension, it is automatically concatenated to the filename string).

The last thing that will be asked to input is the number of threads the user wants to run.

## Outputs
The program will print a recap of the informations and the various user inputs and will prompt several requests for the press of the ENTER key, so the user can check the informations with calm.

## Extraction
After the recap, the open windows will get minimized to show only the desktop where a JFrame for every thread will show the progress in the checking of the combinations.

IMPORTANT: Some threads may work faster than others due to several factors such as how the processor handles them.

If the windows showing the progress of the keyword check close without the progress bars reaching 100% it means the archive got extracted in the folder it got placed in the beginning.

---

# Links
[GitHub](https://github.com/Fil0611)