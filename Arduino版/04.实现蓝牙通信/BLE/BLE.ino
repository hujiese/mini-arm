int val;
int ledpin = 13;
void setup()
{
   Serial.begin(9600);
  pinMode(ledpin, OUTPUT);
}

void loop()
{
   val = Serial.read();
  if(val == 'a')
 {
     digitalWrite(ledpin, HIGH);
     delay(500);
     digitalWrite(ledpin, LOW);
     delay(500);
     Serial.println("Received !");
 } 
}
