#include <SoftwareSerial.h>

// 0,1,2---->IR,Turn,Notification

// Definitions for variables and pins------------
#define RX_PIN 10 // Goes to the TX PIN
#define TX_PIN 9 // Goes to the RX PIN
#define Number_Of_Features 4//[Blindspot,Turning,Braking,Notifications]
#define BLIND_TEST 5
#define Turn_Test 6
#define Brake_Test 7
#define Noti_Test 8

int feature_index;
int counter = 0;
bool Features[Number_Of_Features] = {1,1,1,1};
//-----------------------------------------------


SoftwareSerial CycleBlue = SoftwareSerial(RX_PIN,TX_PIN);

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  CycleBlue.begin(9600);

  pinMode(BLIND_TEST,OUTPUT);
  pinMode(Turn_Test,OUTPUT);
  pinMode(Noti_Test,OUTPUT);
  pinMode(Brake_Test,OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  
  if(CycleBlue.available()>0)
    {
      Bluetooth_Handler();
    }

   if(Features[0] == 1)
    {
      digitalWrite(BLIND_TEST,HIGH);
      //Serial.println("IR ON");
      
    }else
    {
      digitalWrite(BLIND_TEST,LOW);
    }
    
    if(Features [1] == 1)
    {
      digitalWrite(Turn_Test,HIGH);
      //Serial.println("Turning ON");
      
    }else
    {
      digitalWrite(Turn_Test,LOW);
    }
    
    if(Features[2] == 1)
    {
      digitalWrite(Brake_Test,HIGH);
      //Serial.println("brake ON");
    }else
    {
       digitalWrite(Brake_Test,LOW);
    }
    
    if(Features[3] == 1)
    {
      digitalWrite(Noti_Test,HIGH);
      Battery_Notification();
      //Serial.println("Notify ON");
    }
    else
    {
      
      digitalWrite(Noti_Test,LOW); 
    }

    

}


void Bluetooth_Handler(){
  
  
  feature_index = CycleBlue.read();

  Features[feature_index] = !Features[feature_index];

}

void Battery_Notification()
{
  if(counter == 0){
    counter++;
    CycleBlue.write("b"); 
  }
  
  
}
