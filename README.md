SKWEEK
======

=A bytebeat machine in Java=

**WARNING:** This app makes loud, unpredictable digital audio noises. Keep your 
volume down when testing. I'm not responsible for blowned eardrums, speakers, 
headphones, nor for making your pets go crazy, or making babies cry.

==Usage==
* Type expression and press `play` (as of today, one would have to press `stop` 
before typing another expression)
* Use the variable `t` at least once in your expression
* Use numbers, even decimals
* Use these operators: `+ - * / ^ << >> | &`
* Group expressions with `( )`
* Use `x`, `y`, and `z` in your expression and control them in real time with
their appropriate sliders
* Use `len` to adjust the length of the loop
* Use `<-...->` for a weird, pseudo pitch shift effect

This is the first iteration of SKWEEK. It is not perfect in any way and there 
are many ways that it can improved. It does not check for expressions with 
illegal character, incomplete paranthesis, signed variables (starting an 
expression with a negative number (i.e. -4) will not be good). 

==TODO==
* Real time typing, evaluating, and processing of expressions
* Add export to audio file
* Save and load expressions
* Volume control (maybe)
* More tweaks
* Port to Android (once)