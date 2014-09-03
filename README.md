SKWEEK
======

=A bytebeat machine in Java=

**WARNING:** This app makes loud, unpredictable digital audio noises. Keep your 
volume down when testing. I'm not responsible for blowned eardrums, speakers, 
headphones, nor for making your pets go crazy or babies cry.

==Usage==
* Type expression and press `play`; update expression and press `enter` on 
keyboard to hear changes
* Use the variable `t` at least once in your expression
* Use numbers, even decimals
* Use these operators: `+ - * / % ^ << >> | &`
* Group expressions with `( )`
* Use `x`, `y`, and `z` in your expression and control them in real time with
their appropriate sliders
* Use `<>` for a weird, pseudo pitch shift effect

This is the first iteration of SKWEEK. It is not perfect in any way and there 
are many ways that it can be improved. For instance, it does not check for expressions with
illegal character, incomplete parenthesis.

There is buggy support for negative numbers.

==Update==
* Removed lots of unnecessary casting
* Removed the 'len' slider and functionality as this was more of a workaround for some gaps in audio, hence...
* Fixed audio gaps

==TODO==
* Real time typing, evaluating, and processing of expressions - (added, but 
still in the works)
* Add export to audio file
* Save and load expressions
* Volume control (maybe)
* More tweaks
* Port to Android (once)
