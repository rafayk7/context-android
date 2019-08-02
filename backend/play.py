msg = "Sent from your Twilio trial account - 2en<>fr<>good morning<>Bonjour"
msg = msg.replace("Sent from your Twilio trial account -", '').strip()

reqType = msg[0]
mainComps = msg[1:].split(";")
titleComps = mainComps[0].split("<>")
mainComps = mainComps[1:]

for comp in mainComps:
    comp = comp.split("<>")
    print("main comp - " + str(comp))

print("reqType - " + reqType)
print("titleComp " + str(titleComps))