import RaidsUnlimitedClient from "../api/raidsUnlimitedClient";
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view profile page of the website.
 */
class ViewProfile extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['loadProfile', 'mount'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
    }


    /**
     * Add the header to the page and load the RaidClient
     */
    mount() {
        this.header.addHeaderToPage();
        this.client = new RaidsUnlimitedClient();
        this.loadProfile();
    }

    async loadProfile() {

            const urlParams = new URLSearchParams(window.location.search);
            const userId = urlParams.get('id');

            const response = await this.client.getProfile(userId);

            document.getElementById('displayName').innerText = response.displayName;
            document.getElementById('warcraftLink').href = response.logs;
            document.getElementById('warcraftLogsLink').innerText = "Link to Warcraft Logs";


            //need to create a table here if it doesn't exist
            let charactersTable = document.getElementById('characterTable');
            if (!charactersTable) {
                charactersTable = document.createElement('table');
                charactersTable.setAttribute('id', 'characterTable');

                // noinspection DuplicatedCode
                const thead = document.createElement('thead');
                const headerRow = document.createElement('tr');
                ['Name', 'Class', 'Specialization', 'Role'].forEach(header => {
                    const th = document.createElement('th');
                    th.textContent = header;
                    headerRow.appendChild(th);
                });
                thead.appendChild(headerRow);
                charactersTable.appendChild(thead)
                document.getElementById('characterList').appendChild(charactersTable);

            }

            const tbody = charactersTable.querySelector('tbody') ||
                charactersTable.appendChild(document.createElement('tbody'));
            response["characterList"].forEach(character => {
                const dataRow = document.createElement('tr');
                [character.charName, character.charClass, character.specialization, character.role].forEach(value => {
                    const td = document.createElement('td');
                    td.textContent = value;
                    dataRow.appendChild(td);
                });
                tbody.appendChild(dataRow);
            });

    }
}

/**
 *
 * Main method to run when the page contents have loaded.
 */
const main = async() => {
    const userProfile = new ViewProfile();
    userProfile.mount();
};
window.addEventListener('DOMContentLoaded', main);




