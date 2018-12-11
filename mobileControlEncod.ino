/*
 * 4 Wheel Mobile Robot 
 * 
 * Tzivaras Vasilis, 2017
 * vtzivaras (at) gmail.com
 */
 
#include <AFMotor.h>
#include <Servo.h>
#include <math.h>

#define echoPin 24  // Echo Pin of the ultrasonic distance sensor
#define trigPin 26  // Trigger Pin of the ultrasonic distance sensor
#define LEDPin 13   // Onboard LED
 pinMode(46, OUTPUT);


AF_DCMotor leftFrontMotor(1);   // M1 - FRONT LEFT motor
AF_DCMotor rightFrontMotor(2);  // M2 - FRONT RIGHT motor
AF_DCMotor leftBackMotor(3);    // M3 - BACK LEFT motor
AF_DCMotor rightBackMotor(4);   // M4 - BACK RIGHT motor

int maximumRange = 200;   // Maximum range needed for the ultrasonic sensor
int minimumRange = 0;     // Minimum range needed for the ultrasonic sensor

long duration, distance=0; // Duration used to calculate distance
int leftMotorSpeed = 5;
int rightMotorSpeed = 5;

boolean MOVING=true;
Servo servo;

void frontServoTest() {
  int i=0;
    Serial.print("Servo test...");
    for(i=0; i<180; i++) {
      servo.write(i);
      delay(10);
    }
    for(i=180; i>0; i--) {
      servo.write(i);
      delay(10);
    }
    servo.write(90);
    Serial.println("OK");
}
void setup() {
  Serial.begin(9600);           // set up Serial library at 9600 bps
  Serial.println("Mobile robot up and ready...");
  
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  servo.attach(10);
  /*
  // Turn motors on
  leftFrontMotor.setSpeed(200);
  rightFrontMotor.setSpeed(200);
 leftBackMotor.setSpeed(200);
  rightBackMotor.setSpeed(200);
  
  leftFrontMotor.run(RELEASE);
  rightFrontMotor.run(RELEASE);
leftBackMotor.run(RELEASE);
  rightBackMotor.run(RELEASE);
  
  digitalWrite(trigPin, LOW); 
  delayMicroseconds(2); 
  
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10); 
  
  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH);*/
  
  //Calculate the distance (in cm) based on the speed of sound.
  //distance = duration/58.2;
  frontServoTest();
  delay(3000);
}

void updateFrontDistance() {
  digitalWrite(trigPin, LOW); 
  delayMicroseconds(2); 
  
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10); 
  
  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH);
  
  //Calculate the distance (in cm) based on the speed of sound.
  distance = duration/58.2;
  /*if( abs(distance - prevDistance) > 10 ) {
    distance = prevDistance;
  }else {
    prevDistance = distance;
  }
  Serial.print("Front distance: ");
  Serial.println(distance);
  */
}

void turnLeft(){
  uint8_t i;
  Serial.println("\tTurning left...");
  
  rightFrontMotor.run(BACKWARD);
  leftFrontMotor.run(FORWARD);
  rightBackMotor.run(BACKWARD);
  leftBackMotor.run(FORWARD);
  

  for (i=0; i<80; i++) {
    rightFrontMotor.setSpeed(i); 
    leftFrontMotor.setSpeed(i); 
    rightBackMotor.setSpeed(i); 
    leftBackMotor.setSpeed(i); 
    delay(20);
  }
}

void turnRight(){
  uint8_t i;
  Serial.println("\tTurning right...");
  
  rightFrontMotor.run(FORWARD);
  leftFrontMotor.run(BACKWARD);
  rightBackMotor.run(FORWARD);
  leftBackMotor.run(BACKWARD);
  

  for (i=0; i<80; i++) {
    rightFrontMotor.setSpeed(i); 
    leftFrontMotor.setSpeed(i); 
    rightBackMotor.setSpeed(i); 
    leftBackMotor.setSpeed(i); 
    delay(20);
  }
}

void turnBack(){
  uint8_t i;
  Serial.println("\tTurning right...");
  
  rightFrontMotor.run(FORWARD);
  leftFrontMotor.run(BACKWARD);
  rightBackMotor.run(FORWARD);
  leftBackMotor.run(BACKWARD);
  

  for (i=0; i<80; i++) {
    rightFrontMotor.setSpeed(i); 
    leftFrontMotor.setSpeed(i); 
    rightBackMotor.setSpeed(i); 
    leftBackMotor.setSpeed(i); 
    delay(20);
  }
}

void moveForward(int vehicleSpeed){
  uint8_t i;
  Serial.println("\tMoving forward...");
  
  rightFrontMotor.run(FORWARD);
  leftFrontMotor.run(FORWARD);
  rightBackMotor.run(FORWARD);
  leftBackMotor.run(FORWARD);
  
  if(MOVING==true) {
    for (i=0; i<vehicleSpeed; i++) {
      rightFrontMotor.setSpeed(i); 
      leftFrontMotor.setSpeed(i); 
      rightBackMotor.setSpeed(i); 
      leftBackMotor.setSpeed(i); 
      delay(20);
   }
   MOVING=false;
  }
}


void activateBrakes(){
  Serial.println("\tStoping...");
  leftFrontMotor.run(RELEASE);
  rightFrontMotor.run(RELEASE);
  leftBackMotor.run(RELEASE);
  rightBackMotor.run(RELEASE);

}

// Turns the servo 180 degrees and finds the position that the mobile can go forward
int decidePath() {
  int leftDistance, rightDistance;
    servo.write(180);
    delay(1000);
    updateFrontDistance();
    leftDistance = distance;
  
    servo.write(0);
    delay(1000);
    updateFrontDistance();
    rightDistance = distance;
    servo.write(90);
    delay(1000);
    if( (leftDistance > 35) || (leftDistance == -1 ) ) {
      return 1;
    }else if( (rightDistance > 35) || (rightDistance == -1 ) ) {
      return 3;
    }else {
      return 2;
    }
}


void demoController() {
  int path; // 1 for left, 2 for back, 3 for right
    if ( distance <= 35 ) {  // Object detected
      activateBrakes();
      path = decidePath();
      if(path == 1) {
        Serial.println("Turning left");
        turnLeft();
        delay(2000);
      }else if(path == 2) {
        Serial.println("Turning right");
        turnRight();
        delay(2000);
      }else if(path == 3) {
        Serial.println("Turning back");
        turnBack();
        delay(4000);
      }else {
        activateBrakes();
      }
      
    }else {
      Serial.println("Moving forward");
      moveForward(100);
    }
}

void loop() {
  //updateFrontDistance();
  //demoController();
  moveForward(100);
}
