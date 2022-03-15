//Pt library used for protothreading
#include <pt.h>




// Definitions for variables and pins
#define Turning_Frequency 1000
#define Turning_State_Control_Thres 600
#define TURN_INTERUPT 3 // On our board Pin 3 is an interupt pin would need to check for other boards
#define Analog_Turn_Button A1
#define Left_Turn_LED
#define Right_Turn_LED

int val;
int val2;
int val3;
//------------------------------------
//Interrupt triggers
volatile byte leftTurnS = LOW;
volatile byte turn = LOW;
volatile byte rightTurnS = LOW;

//Decleration of protothreading variables and functions
//Pt pointers to be used
static struct pt pt_Left_Turn, pt_Right_Turn ;


//Left Turn function to turn on the the left turn light, at a 1Hz frequency
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


//Left Turn function to turn on the the Right turn light, at a 1Hz frequency
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



//-----------------------------------------------------
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);

  //Init of protothreading functions
  PT_INIT(&pt_Left_Turn);
  PT_INIT(&pt_Right_Turn);

  //Init the interupts pins
  pinMode(TURN_INTERUPT,INPUT);
  attachInterrupt(digitalPinToInterrupt(TURN_INTERUPT), stateTurn, RISING);
}

void loop() {
  // put your main code here, to run repeatedly:


  leftTurn(&pt_Left_Turn);
  rightTurn(&pt_Right_Turn);
  
}


int max_local(int x, int y, int z){
  return(max(max(x,y),z));
}


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
