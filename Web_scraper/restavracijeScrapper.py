import requests
from bs4 import BeautifulSoup
import json


def json_from_html_using_bs4(base_url):
	page = requests.get(base_url)

	soup = BeautifulSoup(page.content, "html.parser")
	res =[]
	restaurants = soup.findAll('div', class_="restaurant-row")

	for restaurant in restaurants:
		if(restaurant['data-city'] == 'MARIBOR'):
			links = restaurant.find('a')
			imeLokala = links.get_text('', strip=True)
			cenaBrezBona = restaurant['data-cena']
			cenaZBonom = restaurant['data-doplacilo']
			latitude = restaurant['data-lat']
			longitude = restaurant['data-lon']
			loc = [float(latitude), float(longitude)]
			location = restaurant['data-naslov'] + ', Maribor'
			ponudbaPoVrsti = [['Meso', False], ['Vegetarijansko', False], ['Riba', False], ['Mešano', False], ['Solata', False], ['Pizza', False], ['Hitra hrana', False], ['Celiakiji prijazni obroki', False], ['Špageti', False], ['Burger', False], ['Sendvič', False], ['Juha', False], ['Burek', False], ['Raca', False], ['Piščanec', False], ['Svinjina', False], ['Govedina', False], ['Lazanja', False]]

			link = links['href']
			secondPage = requests.get("https://www.studentska-prehrana.si" + link)
			soup = BeautifulSoup(secondPage.content, "html.parser")

			workDay = soup.find('div', class_="col-md-12 text-bold")
			delovniCas = ['0:00'] * 7

			delo = workDay.get_text().strip().replace(" ", "")
			delo1 = delo.splitlines()
			for dl in delo1:
				if(len(dl) != 0):
					ura = dl.split(":",1)
					if(ura[0] == "Nedelja/Prazniki"):
						delovniCas[6] = ura[1]
					elif(ura[0] == "Sobota"):
						delovniCas[5] = ura[1]
					else:
						delovniCas[0] = delovniCas[1] = delovniCas[2] = delovniCas[3] = delovniCas[4] = ura[1]

			jediNaMeniju = []
			jedi = soup.findAll('div', class_="shadow-wrapper")
			if(len(jedi) == 1 and "Lokal nima vpisanih menijev." in jedi[0].get_text()):
				continue

			for jed in jedi:
				hrana = jed.find('strong', class_='color-blue')
				opis = jed.findAll('li')
				jedDodatek = []
				for opisek in opis:
					jedDodatek.append(opisek.get_text().strip())
				jediNaMeniju.append([hrana.get_text()[2:].strip(), jedDodatek])

				if("burger" in hrana.get_text()[2:].strip().lower()):
					ponudbaPoVrsti[9][1] = True
				elif(("testenine" or 'špageti') in hrana.get_text()[2:].strip().lower()):
					ponudbaPoVrsti[8][1] = True
				elif("sendvič" in hrana.get_text()[2:].strip().lower()):
					ponudbaPoVrsti[10][1] = True
				elif("juha" in hrana.get_text()[2:].strip().lower()):
					ponudbaPoVrsti[11][1] = True
				elif("burek" in hrana.get_text()[2:].strip().lower()):
					ponudbaPoVrsti[12][1] = True
				elif("raca" in hrana.get_text()[2:].strip().lower()):
					ponudbaPoVrsti[13][1] = True
				elif("pišč" in hrana.get_text()[2:].strip().lower()):
					ponudbaPoVrsti[14][1] = True
				elif("svinj" in hrana.get_text()[2:].strip().lower()):
					ponudbaPoVrsti[15][1] = True
				elif("gove" in hrana.get_text()[2:].strip().lower()):
					ponudbaPoVrsti[16][1] = True
				elif(("lazanja" or "lasagana") in hrana.get_text()[2:].strip().lower()):
					ponudbaPoVrsti[17][1] = True

				if(jed.find('img', class_='pull-right')):
					vrsta = jed.find('img', class_='pull-right')
					for vrstaJedi in ponudbaPoVrsti:
						if(vrsta['title'] == vrstaJedi[0]):
							vrstaJedi[1] = True

			data = {'ime': imeLokala, 'lokacija': location, 'cenaSStudentskimBonom': cenaZBonom, 'cenaBrezStudentskegaBona': cenaBrezBona, 'delovniCas': delovniCas, 'loc': loc, 'ponudbaPoVrstiHrane': ponudbaPoVrsti, 'meni': jediNaMeniju}
			res.append(data)
	return res


if __name__ == "__main__":
    print("Start")
    URL = "https://www.studentska-prehrana.si/sl/restaurant"

    res = json_from_html_using_bs4(URL)

    with open('restavracije.json', 'w', encoding='UTF-8') as f:
        json.dump(res, f, indent=8, ensure_ascii=False)
    print("Finish")
