#include <Servo.h>
Servo mServo;
char val[5] = {'0'};
int len = 0;
int temp = 0;
void setup()
{
    mServo.attach(9);
    Serial.begin(9600);//���ӵ����ж˿ڣ�������Ϊ9600
    Serial.println("servo=o_seral_simple ready" ) ;
}
void loop()//��0 ��9 ����ת��Ϊ0 ��180 �Ƕȣ�����LED ��˸��Ӧ���Ĵ���
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