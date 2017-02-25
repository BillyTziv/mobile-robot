/*
* ArduMobile - An Autonomous Nagivating Mobile
*
* Developed by Vasilis Tzivaras
* contact me at vtzivaras(at)gmail.com
*
* Arduino UNO R3
* HC-SR04 Ultrasonic Sensor
* Motor Shield v2
*/

#define LMDirection 12                  // Hight for forward and LOW for backward
#define RMDirection 13

#define LMSpeed 3
#define RMSpeed 11

#define LMBrake 9
#define RMBrake 8

#define frontSensor 10

#define echoPin 4
#define trigPin 5

int maximumRange = 200; 		// Maximum range needed
int minimumRange = 0; 			// Minimum range needed
long duration=0, distance=0; 		// Duration used to calculate distance

void setup() {
	Serial.begin (9600);
	pinMode(trigPin, OUTPUT);
	pinMode(echoPin, INPUT);
	
	pinMode(LMDirection, OUTPUT);
	pinMode(RMDirection, OUTPUT);

	pinMode(LMBrake, OUTPUT);
	pinMode(RMBrake, OUTPUT);
}

int getDistance() {
	digitalWrite(trigPin, LOW); 
	delayMicroseconds(2); 

	digitalWrite(trigPin, HIGH);
	delayMicroseconds(10); 

	digitalWrite(trigPin, LOW);
	duration = pulseIn(echoPin, HIGH);

	//Calculate the distance (in cm) based on the speed of sound.
	distance = duration/58.2;

	if (distance >= maximumRange || distance <= minimumRange){
		//Serial.println("-1");
                return -1;
	}else {
		//Serial.println(distance);
                return distance;
	}
delay(500); 
}

void forward(int speed) {
	//Left motor forward @ speed
	digitalWrite(LMDirection, HIGH);        //Establishes forward direction of motor1
	digitalWrite(LMBrake, LOW);   		//Disengage the Brake for Channel A
	analogWrite(LMSpeed, speed);            //Spins the motor on Channel A at half speed

	//Right motor forward @ speed
	digitalWrite(RMDirection, HIGH);        //Establishes forward direction of motor2
	digitalWrite(RMBrake, LOW);   		//Disengage the Brake for Channel B
	analogWrite(RMSpeed, speed);   		//Spins the motor on Channel B at full speed
}

void backward() {
	//Motor ! backward @ half speed
	digitalWrite(LMDirection, LOW);  //Establishes backward direction of Channel B
	digitalWrite(LMBrake, LOW);   //Disengage the Brake for Channel B
	analogWrite(LMSpeed, 123);    //Spins the motor on Channel B at half speed
	
	//Motor B backward @ half speed
	digitalWrite(RMDirection, LOW);  //Establishes backward direction of Channel B
	digitalWrite(RMBrake, LOW);   //Disengage the Brake for Channel B
	analogWrite(RMSpeed, 123);    //Spins the motor on Channel B at half speed
}

void turn() {
	//Motor A forward @ full speed
	digitalWrite(12, LOW);  //Establishes backward direction of Channel A
	digitalWrite(9, LOW);   //Disengage the Brake for Channel A
	analogWrite(3, 123);    //Spins the motor on Channel A at half speed

	//Motor B backward @ half speed
	digitalWrite(13, LOW);  //Establishes backward direction of Channel B
	digitalWrite(8, LOW);   //Disengage the Brake for Channel B
	analogWrite(11, 123);    //Spins the motor on Channel B at half speed
}

void printStatus() {
	Serial.print("Distance: ");
        Serial.print(getDistance());
        Serial.print("\tDirection");
        Serial.print("-");
        Serial.println();
}

void loop() {
        printStatus();
        forward(156);
	if(distance < 15) {
                digitalWrite(LMBrake, HIGH);
                digitalWrite(RMBrake, HIGH);
	}
}

