#include <Servo.h>
Servo mServo;
char val[6];
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
        val[len] = Serial.read();
        len++;
    }
   
   if(len == 5)
   {
     len = 0;
       if(val[0] == 'a' && val[4] == 'z')
       {
               Serial.print(val[0]);
               Serial.print(val[1]);
               Serial.print(val[2]); 
               Serial.print(val[3]);
               Serial.print(val[4]);
               
               val[1] -= '0';
               val[2] -= '0';
               val[3] -= '0';
               temp = val[1] * 100 + val[2] * 10 + val[3];
              mServo.write(temp);
           Serial.println(" ");
       }
   }
        
}
