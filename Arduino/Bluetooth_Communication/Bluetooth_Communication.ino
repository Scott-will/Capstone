#include <SoftwareSerial.h>

// 0,1,2---->IR,Turn,Notification

// Definitions for variables and pins------------
#define RX_PIN 10 // Goes to the TX PIN
#define TX_PIN 9 // Goes to the RX PIN
#define Number_Of_Features 3//[Blindspot,Turning,Notifications]
#define BLIND 5
#define Turn 6
#define Noti 7

int feature_index;
bool Features[Number_Of_Features] = {1,1,1};
//-----------------------------------------------


SoftwareSerial CycleBlue = SoftwareSerial(RX_PIN,TX_PIN);

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  CycleBlue.begin(9600);

  pinMode(BLIND,OUTPUT);
  pinMode(Turn,OUTPUT);
  pinMode(Noti,OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  
  if(CycleBlue.available()>0)
    {
      Bluetooth_Handler();
    }

   if(Features[0] == 1)
    {
      digitalWrite(BLIND,HIGH);
      //Serial.println("IR ON");
      
    }
    if(Features [1] == 1)
    {
      digitalWrite(Turn,HIGH);
      //Serial.println("Turning ON");
      
    }
    if(Features[2] == 1)
    {
      digitalWrite(Noti,HIGH);
      //Serial.println("Notify ON");
    }else
    {
      digitalWrite(BLIND,LOW);
      digitalWrite(Turn,LOW);
      digitalWrite(Noti,LOW); 
    }

    Notify();

}


void Bluetooth_Handler(){
  
  
  feature_index = CycleBlue.read();

  Features[feature_index] = !Features[feature_index];

}

void Notify()
{

  CycleBlue.write("TEST");
  delay(1000);
  
}
