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
        try {
            const urlParams = new URLSearchParams(window.location.search);

            const raidId = urlParams.get('id');
            document.getElementById('raid-name').innerText = "Loading Raid ...";
            const raid = await this.client.getRaid(raidId);
            console.log("Raid object receive: ", raid);
            this.dataStore.set('raid', raid);
        } catch (error) {
            console.error("Error loading client data: ", error);
        }
    }

    /**
     * Add the header to the page and load the RaidClient
     */
    async mount() {
        this.header.addHeaderToPage();

        this.client = new RaidsUnlimitedClient();
        this.clientLoaded();
    }

    addRaidToPage() {
        const raidModel = this.dataStore.get('raid');
        if (raidModel == null) {
            return;
        }

        document.getElementById('raid-name').innerText = "Raid Name: " + raidModel.raidName;
        document.getElementById('raid-size').innerText = "Raid Size: " + raidModel.raidSize;
        document.getElementById('raid-date').innerText = "Raid Date: " + raidModel.raidDate;
        document.getElementById('raid-time').innerText = "Raid Time: " + raidModel.time;
        document.getElementById('raid-server').innerText = "Server: " + raidModel.raidServer;
        document.getElementById('raid-status').innerText = "Raid Status: " + raidModel.raidStatus;

        const participants = raidModel.participants;
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
                </tr>
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




