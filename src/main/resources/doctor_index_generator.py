import names
import random
import json

doctor_index=[]

for id in range(1000):
    doctor_id={"index":{"_id":id}}
    doctor_data={}
    doctor_data["gender"]=random.choice(["male","female"])
    doctor_data["name"]=names.get_first_name(doctor_data["gender"])
    doctor_data["availability"]=random.choice(["always","today","next 3 days","coming weekend"])
    doctor_data["experience"]=random.randint(0,50)
    doctor_data["rating"]=random.randint(1,5)
    doctor_data["relevance"]=random.random()
    doctor_data["online"]=random.choice([True,False])
    doctor_data["price"]=random.uniform(0, 10000)
    
    doctor_index.append(doctor_id)
    doctor_index.append(doctor_data)

with open("doctor_index.json","w") as file:
    for obj in doctor_index:
        file.write(json.dumps(obj)+"\n")