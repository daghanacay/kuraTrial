# kuraTrial

This is a sample code to use Kura for developing GPIO example

In order to make this code work you need to have a working Kra environment with Rapspberry Pi. Instructions can be found here

Set up your development environment environment
http://eclipse.github.io/kura/doc/kura-setup.html

Set up Raspberry Pi
http://eclipse.github.io/kura/doc/raspberry-pi-quick-start.html

Deploy this application
http://eclipse.github.io/kura/doc/deploying-bundles.html Under heading "Remote Target Device"

Your multi color LED should be connected to GPIO pins 13, 19, 26

The LED I am using is similar to 

http://www.banggood.com/RGB-3-Color-LED-Module-For-Arduino-Red-Green-Blue-p-76519.html?currency=AUD&createTmp=1&utm_source=google&utm_medium=shopping&utm_content=saul&utm_campaign=Electronic-xie-au&gclid=Cj0KEQjw-b2wBRDcrKerwe-S5c4BEiQABprW-NBS9NrcwjPHfgesgZCmQCbgdlqLUs3yeEj4x2OeJ2saAiOE8P8HAQ

As soon as you deploy this application to your Rapsberry pi the le should start flashing between 3 different colors. To configure application goto Kura Webconsole at http://[rasperryPi IP]/kura (use login:admin password:admin) and click on the configurable application on the left panel. Play with the configuration and see how it affects the LED behviour.

This code is provided without any warranty whatsoever. Good luck :)
