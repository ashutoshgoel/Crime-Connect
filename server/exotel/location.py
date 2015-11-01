from geopy.geocoders import Nominatim
import sys, json

try:
    data = json.loads(sys.argv[1])
   
    
except:
    print "ERROR"
    sys.exit(1)
    
geolocator = Nominatim()
location = geolocator.reverse(data[0]+','+data[1])
a=location.address

result = {'address': a}

# Send it to stdout (to PHP)
print json.dumps(result)
