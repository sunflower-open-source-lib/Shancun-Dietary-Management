/*

  PN532 reads the tag by Arduino mega/Leonardo
  command list:

  #wake up reader
  send: 55 55 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ff 03 fd d4 14 01 17 00
  return: 00 00 FF 00 FF 00 00 00 FF 02 FE D5 15 16 00

  #get firmware
  send: 00 00 FF 02 FE D4 02 2A 00
  return: 00 00 FF 00 FF 00 00 00 FF 06 FA D5 03 32 01 06 07 E8 00

  #read the tag
  send: 00 00 FF 04 FC D4 4A 01 00 E1 00
  return: 00 00 FF 00 FF 00 00 00 FF 0C F4 D5 4B 01 01 00 04 08 04 XX XX XX XX 5A 00
  XX is tag.

*/

const unsigned char wake[24] = {0x55, 0x55, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xff, 0x03, 0xfd, 0xd4, 0x14, 0x01, 0x17, 0x00}; //wake up NFC module
const unsigned char firmware[9] = {0x00, 0x00, 0xFF, 0x02, 0xFE, 0xD4, 0x02, 0x2A, 0x00}; //
const unsigned char tag[11] = {0x00, 0x00, 0xFF, 0x04, 0xFC, 0xD4, 0x4A, 0x01, 0x00, 0xE1, 0x00}; //detecting tag command

unsigned char receive_ACK[25];//Command receiving buffer
int inByte = 0;               //incoming serial byte buffer

int LED = 13;
long Weight = 0;    //定义一个变量用于存放承重的重量，单位为g
int Weighti = 0;

#include "HX711.h"          //调用24bitAD HX711库 秤
HX711 HX711_CH0(8, 9, 750); //SCK,DT,GapValue
//SCK引脚用于arduino和HX711模块通讯的时序提供
//DT引脚用于从HX711读取AD的数据
//GapValue用于校准输出的重量值，如果数值偏大就加大该值，如果数据偏小就减小该值

#if defined(ARDUINO) && ARDUINO >= 100
#include "Arduino.h"
#define print1Byte(args) Serial1.write(args)
#define print1lnByte(args)  Serial1.write(args),Serial1.println()
#else
#include "WProgram.h"
#define print1Byte(args) Serial1.print(args,BYTE)
#define print1lnByte(args)  Serial1.println(args,BYTE)
#endif

void setup()
{
  Serial2.begin(115200);           //设定串口输出波特率
  Serial.begin(9600); // open serial锛宻et Baund rate 9600 bps
  Serial1.begin(115200);

  //称重
  pinMode(LED, OUTPUT);        //设定LED是输出引脚
  digitalWrite(LED, LOW);     //LED熄灭
  //Serial.print("Welcome to use!\n");
  HX711_CH0.begin();          //读取传感器支架毛重
  delay(3000);                //延时3s用于传感器稳定
  HX711_CH0.begin();          //重新读取传感器支架毛重用于后续计算
  digitalWrite(LED, HIGH);    //板载LED点亮，说明可以承重
  //NFC
  //Serial2.begin(115200);
  wake_card();
  delay(100);
  array_ACK(35);
  delay(100);
  dsplay(15);

}

void loop()
{

  //NFC
  if (Serial2.available() > 0)
  {
    Serial.println("Serial2>0");
    byte getrad[4];
    for (int i = 0; i < 4; i++)
    {
      getrad[i] = Serial2.read();
      //SerialUSB.println(getrad[i]);
    }
    inByte = bytesToInt(getrad, 4);
    // get incoming byte:
    //inByte = Serial2.read();
    if (inByte == 1)//1读卡
    {
      read_tag();
      delay(100);
      Serial.println("start！");
      array_ACK(25);
      delay(100);
      Serial.println("end ");
      dsplay(25);
    }
    else if (inByte == 2)//2写
    {
      firmware_version();
      delay(100);
      array_ACK(19);
      delay(100);
      dsplay(19);
    }
    delay(1000);
  }
  //称重
  Weight = HX711_CH0.Get_Weight();//采样当前传感器重量，该重量已经自动去皮，去皮值根据初始化程序中采样的值计算。
  if (Weight > 0)
  {
    Serial2.write(Weight);     //串口输出当前重量
    Serial2.println(" g");     //单位为g

    Serial.print(Weight);     //串口输出当前重量
    Serial.println(" g");     //单位为g
    delay(1000);
  }
}

void UART1_Send_Byte(unsigned char command_data)
{
  //Serial1.print(command_data);
  print1Byte(command_data);// command send to device
#if defined(ARDUINO) && ARDUINO >= 100
  Serial1.flush();// complete the transmission of outgoing serial data
#endif
}

void UART_Send_Byte(unsigned char command_data)//, unsigned charBYTE
{
  Serial.print(command_data, HEX);
  Serial2.print(command_data, HEX);
  Serial.print(" ");
}


void array_ACK(unsigned char temp)
{
  for (unsigned char i = 0; i < temp; i++)
  {
    receive_ACK[i] = Serial1.read();
    delay(100);
  }
}

void wake_card(void)
{
  unsigned char i;
  for (i = 0; i < 24; i++) //send command
    UART1_Send_Byte(wake[i]);
}

void firmware_version(void)
{
  unsigned char i;
  for (i = 0; i < 9; i++) //send command
    UART1_Send_Byte(firmware[i]);
}

void read_tag(void)
{
  unsigned char i;
  for (i = 0; i < 11; i++) //send command
    UART1_Send_Byte(tag[i]);
}

void dsplay(unsigned char tem)
{
  unsigned char i;
  for (i = 0; i < tem; i++) //send command
    UART_Send_Byte(receive_ACK[i]);
  Serial.println();
}

int bytesToInt(byte* bytes, int size)
{
  int ad = bytes[3] & 0xFF;
  ad |= ((bytes[2] << 8) & 0xFF00);
  ad |= ((bytes[1] << 16) & 0xFF0000);
  ad |= ((bytes[0] << 24) & 0xFF000000);
  return ad;
}

