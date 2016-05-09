#!/usr/bin/python3

from __future__ import print_function
import sys, os

json_prefix = '{\n\t"vehicles": [\n'
json_suffix = "\t]\n}\n"
    
json_content = json_prefix
vehicles_no = 200
start_location = (40, 50)
capacity = 200
qm = '"'
o_ident = '\t' * 2
ident = '\t' * 3
i_ident = '\t' * 4
nl = ",\n"

def gen_vehicle(vehicle_no):
    vehicle_id = 'truck' + str(vehicle_no)
    vehicle_data = o_ident + '{\n' \
        + ident + '"id": ' + qm + vehicle_id + qm + nl \
        + ident + '"maxCapacity": ' + str(capacity) + nl\
        + ident + '"startLocation": {\n' \
        + i_ident + '"x": ' + str(start_location[0]) + nl \
        + i_ident + '"y": ' + str(start_location[1]) + '\n'\
        + ident + "}\n" \
        + o_ident + "}"
        
    return vehicle_data

if len(sys.argv) > 1:
    vehicles_no = int(sys.argv[1])
    print(vehicles_no)

print('Generating data for ' + str(vehicles_no) + ' vehicles')

for i in range(vehicles_no - 1):
    json_content += gen_vehicle(i + 1) + ',\n'

json_content += gen_vehicle(vehicles_no) + '\n'
json_content += json_suffix

filename = 'vehicles.json'
if len(sys.argv) > 2:
    filename = sys.argv[2]
    
f = open(filename, 'w+')
try:
    print(json_content, file=f)
    print('Done! Data saved under: ' + os.path.realpath(f.name))
finally:
    f.close()

