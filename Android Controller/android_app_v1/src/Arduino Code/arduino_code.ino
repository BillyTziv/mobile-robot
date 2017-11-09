#include <SoftwareSerial.h>

SoftwareSerial mySerial(0,1);
String Data = "";

void setup()  
{
    Serial.begin(9600);
    mySerial.begin(9600);
    //Serial.println("Hello world");
}

void loop() // run over and over
{
    while (mySerial.available() > 0)
    {
        //Serial.println("Something is available");
        char character = mySerial.read(); // Receive a single character from the software serial port
        Data.concat(character); // Add the received character to the receive buffer
        
        if (character == '\n')
        {
            //Serial.print("Received: ");
            Serial.println(Data);

            // Add your code to parse the received line here....

            // Clear receive buffer so we're ready to receive the next line
            Data = "";
        }
    }
}
