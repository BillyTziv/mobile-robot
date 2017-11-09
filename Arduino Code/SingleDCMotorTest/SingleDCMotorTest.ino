/*
 * Single DC motor direction and speed test 
 */
 
void setup() {
  pinMode(12, OUTPUT);    //Initiates Motor Channel A pin
  pinMode(9, OUTPUT);     //Initiates Brake Channel A pin
}

void loop(){
  
  //forward @ low speed
  digitalWrite(12, HIGH); //Establishes forward direction of Channel A
  digitalWrite(9, LOW);   //Disengage the Brake for Channel A
  analogWrite(3, 50);     //Spins the motor on Channel A at full speed
  
  delay(3000);
  
  digitalWrite(9, HIGH);  //Eengage the Brake for Channel A

  delay(1000);
  
  //backward @ low speed
  digitalWrite(12, LOW);  //Establishes backward direction of Channel A
  digitalWrite(9, LOW);   //Disengage the Brake for Channel A
  analogWrite(3, 50);     //Spins the motor on Channel A at half speed
  
  delay(3000);
  
  digitalWrite(9, HIGH);  //Eengage the Brake for Channel A
  
  delay(1000);
}

