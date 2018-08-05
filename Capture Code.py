import mysql.connector
import sys
from PIL import Image
import base64
import cStringIO
import PIL.Image
from gpiozero import MotionSensor
from picamera import PiCamera

  camera = PiCamera()
  # Indicating the pin where motion sensor is connected
  pir = MotionSensor(4)
  
while True:
    camera.start_preview()
    pir.wait_for_motion()
    print("Motion detected!")
    camera.capture('home/pi/upload_image.jpg')                              
    camera.stop_preview()
    camera.close()
    db = mysql.connector.connect(user='root', password='6thsense@123',
                              host='35.200.230.112',
                              database='be_project')
    image = Image.open('C:\Users\Deep\Desktop\upload_image.jpg')
    blob_value = open('C:\Users\Deep\Desktop\upload_image.jpg', 'rb').read()
    sql = 'INSERT INTO 6thsense(image) VALUES(%s)'    
    args = (blob_value, )
    cursor=db.cursor()
    cursor.execute(sql,args)
    db.commit()
    db.close()
    time.sleep(15)

