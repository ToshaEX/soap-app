import zeep
from flask import Flask, request, render_template, json, jsonify


app = Flask(__name__)

@app.route("/",methods=["POST","GET"])
def home():
    #display a read country code on a selector list
    data = rates()
    return render_template('index.html',key_values=data)

@app.route("/convert",methods=["POST"])
def convert():
    results = ""
 
    #requesting a the user input data from form
    sourceCurrencyAmount = request.form.get("sourceValue")
    sourceCurrency = request.form.get("sourceCountry")
    targetCurrency = request.form.get("targetCountry")

    print(sourceCurrencyAmount,sourceCurrency,targetCurrency)

    results = callServerSideConverter(sourceCurrency,targetCurrency,sourceCurrencyAmount)
        
    print(results)
    
    #return a result
    return jsonify({'target_value': results})


#read json file and extract a country code
def rates():
                    #rates.json or WebClient/rates.json
    json_file = open("rates.json", "r")
    json_data = json_file.read()
    json_object = json.loads(json_data)
    json_value = json_object['rates']

    keys_arr = []
    for key in json_value.keys():
        keys_arr.append(key)
    return keys_arr

#Called server to calculation
def callServerSideConverter(sourceCurrency,targetCurrency,sourceCurrencyAmount):
    value = ""
    results = ""

    try:
        client = zeep.Client('http://localhost:8888/WebService?wsdl')
        results = client.service.converter(sourceCurrency,targetCurrency,sourceCurrencyAmount)
    except:
        print("Server offline")

    if((str)(results) != 'nan' and (str)(results) != 'infinity'):
        value = (str)(results)
    else:
        value = "target currency"
    return value


if __name__ == '__main__':
    app.run(debug=True)