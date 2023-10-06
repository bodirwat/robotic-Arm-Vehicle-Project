#include <SoftwareSerial.h>
#include <Servo.h>
#define echoPin A1 
#define trigPin A0

SoftwareSerial BTSerial (0 ,1);
Servo myServoA;
Servo myServoB;
Servo myServoC;

int pos = 0;
int motorA = 3;
int motorB = 4;
int motor2A = 5;
int motor2B = 6;
char dir ;
String mode;

long duration; // variable for the duration of sound wave travel
int distance;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  BTSerial.begin(9600);
pinMode(trigPin, OUTPUT); // Sets the trigPin as an OUTPUT
  pinMode(echoPin, INPUT);
  myServoA.attach(9);
   myServoB.attach(10);
    myServoC.attach(11);
  pinMode(motorA, OUTPUT);
  pinMode(motorB, OUTPUT);
  pinMode(motor2A, OUTPUT);
  pinMode(motor2B, OUTPUT);

  //(Optional)
  pinMode(2, OUTPUT); 
  pinMode(7, OUTPUT);
   pinMode(8, OUTPUT);
    pinMode(8 , OUTPUT);
  //(Optional)
}
void Stop();
void left();
void right();
void forward();
void backward();
void loop() {
  // put your main code here, to run repeatedly:

  //Controlling speed (0 = off and 255 = max speed):     

  analogWrite(2, 150); //ENA pin
  analogWrite(7,150  ); //ENB pin
  
 
 //===================================================

  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  // Sets the trigPin HIGH (ACTIVE) for 10 microseconds
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
   duration = pulseIn(echoPin, HIGH);
  // Calculating the distance
  distance = duration * 0.034 / 2; // Speed of sound wave divided by 2 (go and back)
  // Displays the distance on the Serial Monitor
  
  //=======================================================

  char dirCMD;



if (Serial.available()) {
 dir = Serial.read();
  Serial.println(dir);

//=================================================================
//=======================mode 1====================================
//=================================================================

if(dir == '1'){
mode = "servo1";
}
   
if(mode == "servo1" && dir =='b'){

 for (pos = 50; pos <= 150; pos += 1) {
    // in steps of 1 degree
    myServoA.write(pos);             
    delay(15);                       // waits 15 ms for the servo to reach the position
  }



}
if(mode == "servo1" && dir == 'f'){
 
   for (pos = 150; pos >= 50; pos -= 1) { 
    myServoA.write(pos);              
    delay(15);                       
  }
}




//==========================================================================
//==========================mode 2========================================== 
//========================================================================== 
if(dir == '2'){
mode = "servo2";
}
 if(mode == "servo2" && dir =='f'){

 for (pos = 50; pos <= 150; pos += 1) {
    // in steps of 1 degree
    myServoB.write(pos);             
    delay(15);                       // waits 15 ms for the servo to reach the position
  }



}
if(mode == "servo2" && dir == 'b'){
  
   for (pos = 150; pos >= 50; pos -= 1) { 
    myServoB.write(pos);              
    delay(15);                       
  }
 }





  
//==============================================================================

//=======================mode 3=================================================
if(dir == '3'){
mode = "servo3";
}


if(mode == "servo3" && dir =='f'){

for (pos = 50; pos <= 150; pos += 1) { // goes from 0 degrees to 180 degrees
    // in steps of 1 degree
    myServoC.write(pos);              // tell servo to go to position in variable 'pos'
    delay(15);                       // waits 15 ms for the servo to reach the position
  }

}
if(mode == "servo3" && dir == 'b'){
  

 for (pos = 150; pos >= 50; pos -= 1) { 
    myServoC.write(pos);             
    delay(15   );
 } 
}else{

//========================drive mode============================================
  if(dir == '4'){
   mode = "drive"; 
  }
}

  if(mode == "drive" && dir == 's'){
digitalWrite(8,HIGH);
Stop();

   
  }
  if(mode == "drive" && dir == 'l'){
   
    left();
  }
 if(mode == "drive" && dir == 'r'){
   // digitalWrite(8,LOW);
      digitalWrite(8,HIGH);
    right();
  }

   if(mode == "drive" && dir == 'b'){
    digitalWrite(8,LOW);
    backward();
  }
   if(mode == "drive" && dir == 'f'){
    digitalWrite(8,HIGH);
    forward();
  }

  
 }

 if(mode == "drive" && distance <=25   ){
   digitalWrite(8,HIGH);
  Stop();
  delay(1000);
  digitalWrite(8,LOW);
  backward();
  delay(1000);
   Stop();
  
  
 }else{
  digitalWrite(8,LOW);
}
  






  
  
 // left();
}




void backward(){
  digitalWrite(motorA, HIGH);
  digitalWrite(motorB, LOW);
  

  digitalWrite(motor2A, LOW);
  digitalWrite(motor2B, HIGH);
}
void forward(){
   digitalWrite(motorA, LOW);
  digitalWrite(motorB, HIGH);
  digitalWrite(motor2A, HIGH);
  digitalWrite(motor2B, LOW);
   
}

void left(){
  digitalWrite(motorA, HIGH);
  digitalWrite(motorB, LOW);
  digitalWrite(motor2A, LOW);
  digitalWrite(motor2B, LOW);
}

void right(){
  digitalWrite(motorA, LOW);
  digitalWrite(motorB, LOW);
  digitalWrite(motor2A, LOW);
  digitalWrite(motor2B, HIGH);
}

void Stop(){
digitalWrite(motorA, LOW);
  digitalWrite(motorB,LOW);
  digitalWrite(motor2A, LOW);
  digitalWrite(motor2B, LOW);
  
}
