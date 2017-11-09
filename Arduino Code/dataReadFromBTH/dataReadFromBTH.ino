
/*
* ArduMobile - An Autonomous Nagivating Mobile Robot
*
* Developed by Vasilis Tzivaras
* contact me at vtzivaras(at)gmail.com
*
* Arduino UNO R3
* HC-SR04 Ultrasonic Sensor
* Motor Shield v2
* Lipo Battery
* 9V Battery
* Two DC Motors
*/

#include <SoftwareSerial.h>
#include <Servo.h>

#define LMDirection 12
#define RMDirection 13

#define LMSpeed 3
#define RMSpeed 11

#define LMBrake 9
#define RMBrake 8

SoftwareSerial mySerial(0,1);
String Data = "";

int mobileSpeed = 0;

void setup() {
    Serial.begin(9600);
    mySerial.begin(9600);

    Serial.println("Welcome to ardu-explorer v1.0");

    // Motor movement
    pinMode(LMDirection, OUTPUT);
    pinMode(RMDirection, OUTPUT);

    // Motor brake
    pinMode(LMBrake, OUTPUT);
    pinMode(RMBrake, OUTPUT);
}


/*
 * Move forward at given speed
 */
void moveForward(int forwardSpeed) {
  //Left motor forward @ speed
  digitalWrite(LMDirection, HIGH);        //Establishes forward direction of motor1
  digitalWrite(LMBrake, LOW);             //Disengage the Brake for Channel A
  
  //Right motor forward @ speed
  digitalWrite(RMDirection, HIGH);        //Establishes forward direction of motor2
  digitalWrite(RMBrake, LOW);             //Disengage the Brake for Channel B
  
  analogWrite(RMSpeed, forwardSpeed);     //Spins the motor on Channel B at full speed
  analogWrite(LMSpeed, forwardSpeed);     //Spins the motor on Channel A at half speed
}

/*
 * Move backward at given speed
 */
void moveBackward(int backwardSpeed) {
  //Motor ! backward @ half speed
  digitalWrite(LMDirection, LOW);         //Establishes backward direction of Channel B
  digitalWrite(LMBrake, LOW);             //Disengage the Brake for Channel B
  analogWrite(LMSpeed, backwardSpeed);              //Spins the motor on Channel B at half speed

  //Motor B backward @ half speed
  digitalWrite(RMDirection, LOW);         //Establishes backward direction of Channel B
  digitalWrite(RMBrake, LOW);             //Disengage the Brake for Channel B
  analogWrite(RMSpeed, backwardSpeed);              //Spins the motor on Channel B at half speed
}

/*
 * Turn left at given speed
 */
void turnLeft(int turnLSpeed) {
  //Left motor forward @ speed
  digitalWrite(LMDirection, HIGH);        //Establishes forward direction of motor1
  digitalWrite(LMBrake, LOW);       //Disengage the Brake for Channel A
  analogWrite(LMSpeed, turnLSpeed);            //Spins the motor on Channel A at half speed
  
  //Right motor forward @ speed
  digitalWrite(RMDirection, LOW);        //Establishes forward direction of motor2
  digitalWrite(RMBrake, LOW);      //Disengage the Brake for Channel B
  analogWrite(RMSpeed, turnLSpeed);      //Spins the motor on Channel B at full speed
}

/*
 * Turn right at given speed
 */
void turnRight(int turnRSpeed) {
  //Left motor forward @ speed
  digitalWrite(RMDirection, HIGH);        //Establishes forward direction of motor1
  digitalWrite(RMBrake, LOW);       //Disengage the Brake for Channel A
  analogWrite(RMSpeed, turnRSpeed);            //Spins the motor on Channel A at half speed
  
  //Right motor forward @ speed
  digitalWrite(LMDirection, LOW);        //Establishes forward direction of motor2
  digitalWrite(LMBrake, LOW);      //Disengage the Brake for Channel B
  analogWrite(LMSpeed, turnRSpeed);      //Spins the motor on Channel B at full speed
}

void loop() {
    while (mySerial.available() > 0) {
        //Serial.println("Something is available");
        char character = mySerial.read(); // Receive a single character from the software serial port
        Data.concat(character); // Add the received character to the receive buffer
       
        if (character == '\n') {
            //Serial.print("Received: ");
            //Serial.println(Data);
            mobileSpeed = mySerial.parseInt()

            // Add your code to parse the received line here....
            if (Data.substring(0, 1) == "D") {
                Serial.println("Down");
                moveBackward(60);
                delay(3000);
            }else if (Data.substring(0, 1) == "U") {
                Serial.println("Up");
                moveForward(60);
                delay(3000);
            }else if (Data.substring(0, 1) == "L") {
                Serial.println("Left");
                turnLeft(60);
                delay(3000);
            }else if (Data.substring(0, 1) == "R") {
                Serial.println("Right");
                turnRight(60);
                delay(3000);
            }
            
            // Clear receive buffer so we're ready to receive the next line
            Data = "";
        }
    }
}
