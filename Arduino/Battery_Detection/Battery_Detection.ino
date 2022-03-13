#include <time.h>


// Definitions for variables and pins------------
#define Battery_High_Threshold 5
#define Battery_Medium_Threshold 3 
#define Battery_Low_Threshold 1 
#define Battery_Probe_Pin A5 //Needs to be an Analog Pin
#define Battery_Led_High 15
#define Battery_Led_Med 14
#define Battery_Led_Low 13
#define Error_Time 1000 //delay time for the error code,in msec



int Battery_Volts;




//Do not copy these definitions into final sketch as they are included elsewhere need them here to just verify
#define Led_Left_Blindspot 13 //Left BlindSpot LED
#define Led_Right_Blindspot 12 //Right BlindSpot LED
#define Number_Of_Features 4 //[Data Sent,Blindspot,Turning,Notifications]
int Features[Number_Of_Features] = {0,1,1,1};
//-----------------------------------------------
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);

  
  //Init defined pins
  pinMode(Battery_Probe_Pin,INPUT);
  pinMode(Battery_Led_High,OUTPUT);
  pinMode(Battery_Led_Med,OUTPUT);
  pinMode(Battery_Led_Low,OUTPUT);
  //-----------------


  //Do not copy these definitions into final sketch as they are included elsewhere need them here to just verify
  pinMode(Led_Left_Blindspot,OUTPUT);
  pinMode(Led_Right_Blindspot,OUTPUT);

}

void loop() {
  // put your main code here, to run repeatedly:
 Battery_Volts = Battery_Status;
 Battery_Indication(Battery_Volts);

}


//Decleration of Functions-----------------------------------------------------------

int Battery_Status(){

  //Probe the battery to check voltage
  return(analogRead(Battery_Probe_Pin));
  
  
}

void Battery_Indication(int Battery_Volts){

  // Sets the LEDS accordingly
  if( Battery_Volts <= Battery_High_Threshold && Battery_Volts > Battery_Medium_Threshold){
    digitalWrite(Battery_Led_High, HIGH);
    digitalWrite(Battery_Led_Med, HIGH);
    digitalWrite(Battery_Led_Low, HIGH);
     
  }else if(Battery_Volts <= Battery_Medium_Threshold && Battery_Volts > Battery_Low_Threshold){
    digitalWrite(Battery_Led_High, LOW);
    digitalWrite(Battery_Led_Med, HIGH);
    digitalWrite(Battery_Led_Low, HIGH);
  }else if(Battery_Volts <= Battery_Low_Threshold){
    digitalWrite(Battery_Led_High, LOW);
    digitalWrite(Battery_Led_Med, LOW);
    digitalWrite(Battery_Led_Low, HIGH);
    Battery_Notification();
  }
  
}

void Battery_Notification(){

 
  if(Features[3] == 1){
    //Code to send a push notificaiton to the companion app
  }

  Notify_Lights();

}

void Notify_Lights(){
   int i = 0;
    while(i<5){

    digitalWrite(Led_Left_Blindspot,HIGH);
    digitalWrite(Led_Right_Blindspot,LOW);
    delay(Error_Time);
    digitalWrite(Led_Left_Blindspot,LOW);
    digitalWrite(Led_Right_Blindspot,HIGH);
    delay(Error_Time);
    i++;
    
  }
    
}
//-------------------------------------------------------------------------------------
