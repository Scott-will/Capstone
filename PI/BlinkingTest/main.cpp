#include <wiringPi.h>
#include <stdio.h>
#include <time.h>
#include <iostream>
#include <iomanip>
using namespace std;
// LED Pin - wiringPi pin 0 is BCM_GPIO 17.
// we have to use BCM numbering when initializing with wiringPiSetupSys
// when choosing a different pin number please use the BCM numbering, also
// update the Property Pages - Build Events - Remote Post-Build Event command
// which uses gpio export for setup for wiringPiSetupSys
#define	TRIG	23
#define ECHO 17
int distance(void) {
	clock_t start, end;
	double cpu_time_used;
	digitalWrite(TRIG, HIGH);  // On
	start = clock();
	int count = 0;
	cout << "Print a String2" << endl;
	/*while (digitalRead(ECHO) == 0 && count<50) {
		if ((double)((clock() - start)) > 50) {
			digitalWrite(TRIG, LOW);  // On
			cout << "Off" << endl;
		}
		count++;
		cout << count << endl;
	} */
	while(true) {
		cout << digitalRead(ECHO) << endl;
	}
	end = clock();
	cout << start << endl;
	cout << end << endl;
	cout << CLOCKS_PER_SEC<<endl;
	cout << std::setprecision(9) << ((end-start)) << endl;
	return count;
}


int main(void) {
	wiringPiSetupSys();

	pinMode(TRIG, OUTPUT);
	pinMode(ECHO, OUTPUT);
	cout << "Print a String" << endl;
	digitalWrite(TRIG, HIGH);
	//digitalWrite(TRIG, LOW);
	for (;;)
	{
		digitalWrite(TRIG, HIGH); delay(500);
		digitalWrite(TRIG, LOW); delay(500);
		digitalWrite(ECHO, HIGH); delay(500);
		digitalWrite(ECHO, LOW); delay(500);
	}
	return 0;
	return 0;
}