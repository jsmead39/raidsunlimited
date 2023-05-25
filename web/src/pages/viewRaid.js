import RaidsUnlimitedClient from "../api/raidsUnlimitedClient";
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view raid page of the website.
 */
class ViewRaid extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['clientLoaded', 'mount', 'addRaidToPage'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.addRaidToPage);
        this.header = new Header(this.dataStore);
        console.log("viewRaid constructor");
    }

    /**
     * Once the client is loaded, get the raid metadata and participant list.
     */
    async clientLoaded() {
        const urlParams = new URLSearchParams(window.location.search);
        const raidId = urlParams.get('raidId');
        document.getElementById('raid-name').innerText = "Loading Raid ...";
        const raid = await this.client.getRaid(raidId);
        this.dataStore.set('raid', raid);
    }

    /**
     * Add the header to the page and load the RaidClient
     */
    mount() {
        this.header.addHeaderToPage();

        this.client = new RaidsUnlimitedClient();
        this.clientLoaded();
    }

    addRaidToPage() {
        const raid = this.dataStore.get('raid');
        if (raid == null) {
            return;
        }

        document.getElementById('raid-name').innerText = "Raid Name: " + raid.raidName;
        document.getElementById('raid-size').innerText = "Raid Size: " + raid.raidSize;
        document.getElementById('raid-date').innerText = "Raid Date: " + raid.raidDate;
        document.getElementById('raid-time').innerText = "Raid Time: " + raid.time;
        document.getElementById('raid-server').innerText = "Server: " + raid.raidServer;
        document.getElementById('raid-status').innerText = "Raid Status: " + raid.raidStatus;

        const participants = raid.participants;
        if (participants == null) {
            return;
        }

        let participantHtml = '';
        let participant;
        for (participant of participants) {
            participantHtml += `
                <tr>
                    <td>${participant.displayName}</td>
                    <td>${participant.participantClass}</td>
                    <td>${participant.role}</td>
                `;
        }
        document.getElementById('participant-table').innerHTML = participantHtml;
    }
}

/**
 *
 * Main method to run when the page contents have loaded.
 */
const main = async() => {
    const viewRaid = new ViewRaid();
    viewRaid.mount();
};
window.addEventListener('DOMContentLoaded', main);




