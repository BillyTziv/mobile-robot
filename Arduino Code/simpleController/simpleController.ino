/***********************************************************
* ArduMobile - An Autonomous Nagivating Mobile Robot
*
* Developed by Vasilis Tzivaras
* contact me at vtzivaras(at)gmail.com
*
* Arduino UNO R3
* HC-SR04 Ultrasonic Sensor
* Deek Robot Motor Shield v2
* LIPO 12V 1200mah Battery
* Two DC Motors
* One Servo
* ----------------------------
*
* TX attached at pin X
* RX attached at pin X
* Servo attached at pin 6
* 
* HC-SR04 Ultrasonic Distance Sensor test
*   VCC to arduino 5v 
*   GND to arduino GND
*   Echo to Arduino pin 7 
*   Trig to Arduino pin 8
*
************************************************************/
#define echoPin 4
#define trigPin 5
#define LEDPin 13                       // Onboard LED

#include <Servo.h>

Servo frontServo;
int servoPosition = 90;



int maximumRange = 200;                 // Maximum range needed
int minimumRange = 0;                   // Minimum range needed
long duration=0, distance=0;            // Duration used to calculate distance

boolean objFound = false;               // When an abject is found this var is true

void setup() {
  Serial.begin(9600);

  //Setup Channel A
  pinMode(12, OUTPUT);                  //Initiates Motor Channel A pin
  pinMode(9, OUTPUT);                   //Initiates Brake Channel A pin

  //Setup Channel B
  pinMode(13, OUTPUT);                  //Initiates Motor Channel A pin
  pinMode(8, OUTPUT);                   //Initiates Brake Channel A pin

  // Front Servo
  frontServo.attach(6);

  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  pinMode(LEDPin, OUTPUT);              // Use LED indicator (if required)

  delay(2000);

  Serial.println("Arduino is ready...");
}

void getDistance() {
  /* The following trigPin/echoPin cycle is used to determine the
  distance of the nearest object by bouncing soundwaves off of it. */ 
  digitalWrite(trigPin, LOW); 
  delayMicroseconds(2); 
  
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10); 
  
  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH);
 
  //Calculate the distance (in cm) based on the speed of sound.
  distance = duration/58.2;
 
  if (distance >= maximumRange || distance <= minimumRange){
    /* Send a negative number to computer and Turn LED ON 
    to indicate "out of range" */
    Serial.println("Distance error: -1");
    digitalWrite(LEDPin, HIGH); 
  }else {
    /* Send the distance to the computer using Serial protocol, and
    turn LED OFF to indicate successful reading. */
    Serial.print("Nearest object: ");
    Serial.println(distance);
    Serial.print("cm");
    digitalWrite(LEDPin, LOW); 
  }
 
  //Delay 50ms before next reading.
  delay(50);
}

/*
 * Move forward at given speed
 */
void moveForward(int forwardSpeed) {
  //Motor A forward @ given speed
  digitalWrite(12, HIGH);                 // Establishes forward direction of Channel A
  digitalWrite(9, LOW);                   // Disengage the Brake for Channel A
  analogWrite(3, 70);           // Spins the motor on Channel A at given speed
  
  //Motor B forward @ given speed
  digitalWrite(13, HIGH);                 // Establishes forward direction of Channel B
  digitalWrite(8, LOW);                   // Disengage the Brake for Channel B
  analogWrite(11, 200);          // Spins the motor on Channel B at given speed
}

/*
 * Stop whatever you are doing
 */
void stopMoving() {
  digitalWrite(8, HIGH);  //Engage the Brake for Channel A
  digitalWrite(9, HIGH);  //Engage the Brake for Channel B
}

/*
 * Move backward at given speed
 */
void moveBackward(int backwardSpeed) {
  //Motor A backward @ given speed
  digitalWrite(12, LOW);                  // Establishes backward direction of Channel A
  digitalWrite(9, LOW);                   // Disengage the Brake for Channel A
  analogWrite(3, backwardSpeed);           // Spins the motor on Channel A at given speed
  
  //Motor B backward @ given speed
  digitalWrite(13, LOW);                  // Establishes backward direction of Channel B
  digitalWrite(8, LOW);                   // Disengage the Brake for Channel B
  analogWrite(11, backwardSpeed);          // Spins the motor on Channel B at given speed
}

/*
 * Turn left at given speed
 */
void turnLeft(int turnLSpeed) {
  //Motor A forward @ given speed
  digitalWrite(12, HIGH);                 // Establishes forward direction of Channel A
  digitalWrite(9, LOW);                   // Disengage the Brake for Channel A
  analogWrite(3, turnLSpeed);             // Spins the motor on Channel A at given speed
  
  //Motor B backward @ given speed
  digitalWrite(13, LOW);                  // Establishes backward direction of Channel B
  digitalWrite(8, LOW);                   // Disengage the Brake for Channel B
  analogWrite(11, turnLSpeed);            // Spins the motor on Channel B at given speed
}

/*
 * Turn right at given speed
 */
void turnRight(int turnRSpeed) {
  //Motor B forward @ given speed
  digitalWrite(13, LOW);                 // Establishes forward direction of Channel B
  digitalWrite(8, LOW);                   // Disengage the Brake for Channel B
  analogWrite(11, turnRSpeed);            // Spins the motor on Channel B at given speedd
  
  //Motor A backward @ given speed
  digitalWrite(12, LOW);                  // Establishes backward direction of Channel A
  digitalWrite(9, LOW);                   // Disengage the Brake for Channel A
  analogWrite(3, turnRSpeed);             // Spins the motor on Channel A at given speed
}

/*
 * Run the loop
 */
void loop() {
  // Update distance variable with the current sonar distance
  getDistance();

  // Check if there is any obstacle in front of the mobile
  if( (distance > 2) && (distance < 10) ) {
    // Stop for 2 sec and think.
    stopMoving();
    delay(2000);
    
    // An obstacle is found in 10 cm away, do something and avoid it
    turnRight(120);
    
    // Keep turning for 2 sec.
    delay(2000);

    // Route changed keep going forward.
  }else{ 
    // Free way, move forward at 90 speed.
    moveForward(150);
  }
  
  delay(20);
}
