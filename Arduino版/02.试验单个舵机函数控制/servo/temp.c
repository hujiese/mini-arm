#include <Servo.h>
Servo mServo;
char val[5] = {'0'};
int len = 0;
int temp = 0;
void setup()
{
    mServo.attach(9);
    Serial.begin(9600);//连接到串行端口，波特率为9600
    Serial.println("servo=o_seral_simple ready" ) ;
}
void loop()//将0 到9 的数转化为0 到180 角度，并让LED 闪烁相应数的次数
{
    while(Serial.available())
    {
        len = 0;
        val[len] = Serial.read();
        len++;
    }
   switch(len)
   {
       case 1:
       {
           val[0] -= '0';
           val[1] = 0;
           val[2] = 0;
       }break;
       case 2:
       {
           val[0] -= '0';
           val[1] -= '0';
           val[2] = 0;
       }break;
       case 3:
       {
           val[0] -= '0';
           val[1] -= '0';
           val[2] -= '0';
       }break;
       default:break;
   }
   
       temp = val[2]*100 + val[1]*10 + val[0];
        mServo.write(temp);
        Serial.print("moving servo to ");
        Serial.print(temp);
        Serial.println();
}