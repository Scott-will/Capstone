#include <SharpIR.h>

/*ProtoThreading file
 * Primary file for teh arduino program to control all periphals
 * Written by group
 * 
 */ //Pt library used for protothreading
 
#include <pt.h>


/* Model : Model numbers for different Sharp sensors needed to config the IR sensor
  GP2Y0A02YK0F --> 20150
  GP2Y0A21YK0F --> 1080
  GP2Y0A710K0F --> 100500
  GP2YA41SK0F --> 430
*/


// Definitions for variables and pins------------
#define model 100500
#define IR_Left_Pin A0 
#define IR_Right_Pin A1
#define IR_Normalization 5
#define IR_Lower_Bound 60
#define IR_Upper_Bound 200
#define Led_Left_Blindspot 13 //Left BlindSpot LED
#define Led_Right_Blindspot 12 //Right BlindSpot LED
#define Turning_Frequency 1000
#define Turning_State_Control_Thres 600
#define TURN_INTERUPT 3 // On our board Pin 3 is an interupt pin would need to check for other boards
#define Analog_Turn_Button A1
#define Left_Turn_LED 9
#define Right_Turn_LED 4
#define Number_Of_Features 4 //[Data Sent,Blindspot,Turning,Notifications]
#define Number_Of_Attempts 5


int val;
int val2;
int val3;
int Connection_Attempts = 0;
int distance_left_cm;
int distance_right_cm;
int Features[Number_Of_Features] = {0,1,1,1};
//-----------------------------------------------

//Decleration of ProtoThreading Structures------------------

static struct pt pt_Left_Blind, pt_Right_Blind, pt_Left_Turn, pt_Right_Turn; 


//----------------------------------------------------------



//Interrupt triggers-------------------
volatile byte leftTurnS = LOW;
volatile byte turn = LOW;
volatile byte rightTurnS = LOW;

//-------------------------------------


//Sharp IR Sensor Init--------------------------------
SharpIR myLeftSensor = SharpIR(IR_Left_Pin, model);
SharpIR myRightSensor = SharpIR(IR_Right_Pin, model);
//----------------------------------------------------



// Decleration of Protothreading Functions--------------------

//Left Turn function to turn on the the left turn light, at a Turning_Frequency frequency
static int leftTurn(struct pt *pt){
  static unsigned long lastTimeBlinkL=0;
  PT_BEGIN(pt);
  while(1){
    
      lastTimeBlinkL=millis();
      //10 msec delay to allow other threads to be run
      PT_WAIT_UNTIL(pt,millis()-lastTimeBlinkL>10);
      
   if(leftTurnS==HIGH && rightTurnS==LOW){
    
      lastTimeBlinkL=millis();
      Serial.println("Turning Left");
      PT_WAIT_UNTIL(pt,millis()-lastTimeBlinkL>Turning_Frequency);
      digitalWrite(Left_Turn_LED,HIGH);
      lastTimeBlinkL=millis();
      PT_WAIT_UNTIL(pt,millis()-lastTimeBlinkL>Turning_Frequency);
      digitalWrite(Left_Turn_LED,LOW);
      
    }
    
  }

  PT_END(pt);
}

//Right Turn function to turn on the the Right turn light, at a Turning_Frequency frequency
static int rightTurn(struct pt *ptR){
  static unsigned long lastTimeBlinkR=0;
  PT_BEGIN(ptR);
  while(1){
    
      lastTimeBlinkR=millis();
      PT_WAIT_UNTIL(ptR,millis()-lastTimeBlinkR>10);
      
   if(rightTurnS==LOW && leftTurnS == HIGH){
    
    Serial.println("Turning Right");
      lastTimeBlinkR=millis();
      PT_WAIT_UNTIL(ptR,millis()-lastTimeBlinkR>Turning_Frequency);
      digitalWrite(Right_Turn_LED,HIGH);
      lastTimeBlinkR=millis();
      PT_WAIT_UNTIL(ptR,millis()-lastTimeBlinkR>Turning_Frequency);
      digitalWrite(Right_Turn_LED,LOW);
      
    }
    
  }

  PT_END(ptR);
}


//BlindSpot LED protothreading function, Only need the one function because we sent the LED_PIN as an argument to handel both the left and right sides
//IR_Normalization is used to determine blinking frequency can be changed with the defninition up above
static int BlindSpot(struct pt *ptB, int IR_distance, int LED_PIN){
  static unsigned long lastTimeBlinkB=0;
  PT_BEGIN(ptB);
  
  while(1){
    
      lastTimeBlinkB=millis();
      PT_WAIT_UNTIL(ptB,millis()-lastTimeBlinkB>IR_distance/IR_Normalization);
      lastTimeBlinkB=millis();
      PT_WAIT_UNTIL(ptB,millis()-lastTimeBlinkB>IR_distance/IR_Normalization);
      digitalWrite(LED_PIN,HIGH);
      lastTimeBlinkB=millis();
      PT_WAIT_UNTIL(ptB,millis()-lastTimeBlinkB>IR_distance/IR_Normalization);
      digitalWrite(LED_PIN,LOW);
    
  }

  PT_END(ptB);
}

//-------------------------------------------------------------------------------------



void setup() {
  Serial.begin(9600);
  //Init defined pins
  pinMode(Led_Left_Blindspot,OUTPUT);
  pinMode(Led_Right_Blindspot,OUTPUT);
  pinMode(Left_Turn_LED,OUTPUT);
  pinMode(Right_Turn_LED,OUTPUT);
  //-----------------
  
  //Init of protothreading functions
  PT_INIT(&pt_Left_Turn);
  PT_INIT(&pt_Right_Turn);
  PT_INIT(&pt_Left_Blind);
  PT_INIT(&pt_Right_Blind);
  //--------------------------------

  //Init the interupts pins
  pinMode(TURN_INTERUPT,INPUT);
  attachInterrupt(digitalPinToInterrupt(TURN_INTERUPT), stateTurn, RISING);
  //-----------------------

}

void loop() {
  // put your main code here, to run repeatedly:

  //Upon Reset this loop will run until a connection is established and then it will either grab the data from the phone over bluetooth or it will assume no connection can be 
  //established and assume all functions are to remain on
  while(Connection_Attempts < Number_Of_Attempts){
    //This is where the bluetooth reading will come into play

    if(Features[0] == 1){
      //Features array takes all the data sent and stores it
      break;
    }
    else{
      //Featuers array keeps default values
    }
    Connection_Attempts++;
  }

  if(Features[1] == 1){
  
  IR_Left_Sensor();
  IR_Right_Sensor();
  
  }
  else if(Features[2] == 1){
    
  leftTurn(&pt_Left_Turn);
  rightTurn(&pt_Right_Turn);  
  
  }
  else if(Features[3]==1){
    //Send notifications based on conditions
  }

}

//Decleration of Functions-----------------------------------------------------------

// Left IR Detection function, finds distance compares to threshold and branches based on threshold
void IR_Left_Sensor(){
  // Get a distance measurement and store it as distance_cm:
  distance_left_cm = myLeftSensor.distance();
  
  // Print the measured distance to the serial monitor:
  Serial.print("LEFT Mean distance: ");
  Serial.print(distance_left_cm);
  Serial.println("cm");
    if(distance_left_cm < IR_Upper_Bound && distance_left_cm > IR_Lower_Bound)
  {
    BlindSpot(&pt_Left_Blind,distance_left_cm,Led_Left_Blindspot);
      
  }else
  {
    digitalWrite(Led_Left_Blindspot,LOW);
    //Serial.println("Nothing in me blindspot");
  }
}

// Right IR Detection function, finds distance compares to threshold and branches based on threshold
void IR_Right_Sensor(){
  // Get a distance measurement and store it as distance_cm:
  distance_right_cm = myRightSensor.distance();
  
  // Print the measured distance to the serial monitor:
  Serial.print("Right Mean distance: ");
  Serial.print(distance_right_cm);
  Serial.println("cm");
    if(distance_right_cm < IR_Upper_Bound && distance_right_cm > IR_Lower_Bound)
  {
    BlindSpot(&pt_Right_Blind,distance_right_cm,Led_Right_Blindspot);
      
  }else
  {
    digitalWrite(Led_Right_Blindspot,LOW);
    //Serial.println("Nothing in me blindspot");
  }
}


//Function that returns the max of three variables used to stabilize the analog read used in our turning interupt
int max_local(int x, int y, int z){
  return(max(max(x,y),z));
}


//Function that determins which turning state to go into, uses the Turning_State_Control_Threshold to determine if left or right turn was hit, value based on the resistors used 
// in the circuit
void stateTurn(){
  
  val = analogRead(Analog_Turn_Button);
  //Serial.print("val:");
  //Serial.println(val);
  
  val2 = analogRead(Analog_Turn_Button);
  //Serial.print("val2:");
  //Serial.println(val2);
  
  val3 = analogRead(Analog_Turn_Button);
  //Serial.print("val3:");
  //Serial.println(val3);
  val = max_local(val,val2,val3);
  
  if(val>Turning_State_Control_Thres){
    leftTurnS=!leftTurnS;
    rightTurnS=LOW;
  }else{
    rightTurnS=!rightTurnS;
    leftTurnS=LOW;
  }
  
}


void stateLeftTurn() {
  leftTurnS = !leftTurnS;
  Serial.println("I am in state change");
}


void stateRightTurn() {
  rightTurnS = !rightTurnS;
  Serial.println("I am in right state change");
}
//-------------------------------------------------------------------------------------
