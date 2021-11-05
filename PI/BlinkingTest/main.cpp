#include <wiringPi.h>
#include <stdio.h>
#include <time.h>
#include <iostream>
#include <iomanip>
#include <chrono>
#include <thread>

using namespace std;

// LED Pin - wiringPi pin 0 is BCM_GPIO 17.
// we have to use BCM numbering when initializing with wiringPiSetupSys
// when choosing a different pin number please use the BCM numbering, also
// update the Property Pages - Build Events - Remote Post-Build Event command
// which uses gpio export for setup for wiringPiSetupSys
#define	TRIG	17
#define ECHO 18

double distance(void) {
	double cpu_time_used;
	int count;
	digitalWrite(TRIG, HIGH);  // On
	std::this_thread::sleep_for(std::chrono::microseconds(5));
	digitalWrite(TRIG, LOW);  // Off
	double start = clock();
	while (digitalRead(ECHO) == 0) {
		start = clock();
	}
	double end = clock();
	while (digitalRead(ECHO) == 1) {
		end = clock();
	}
	double time_span = end - start;
	double distance = (time_span / 1e6) * (34300) / 2;
	return distance;
}


/*int main(void) {
	wiringPiSetupSys();
	pinMode(TRIG, OUTPUT);
	pinMode(ECHO, INPUT);
	cout << "Print a String" << endl;

	distance();
	digitalWrite(TRIG, HIGH);
	while (true) {
		cout << distance() << endl;
		delay(500);

	}
	return 0;
}*/