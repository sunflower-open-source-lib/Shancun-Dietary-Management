#ifndef __HX711__H__
#define __HX711__H__

#include <Arduino.h>


class HX711
{
public:
	HX711(int SCK_PIN,int DT_PIN,float GapValueIn=44);
	long Get_Weight();
  int Get_Weight_int();
	void begin();
	int Pressed(int AlarmValue);

	int HX711_SCK;
	int HX711_DT;
	float ValueGap;
	long HX711_Buffer;
	long Weight_Maopi;
	long Weight_Shiwu;
	int CurrentAlarm;
private:	
	void Get_Maopi();
	unsigned long HX711_Read();

};

#endif
