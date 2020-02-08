let key = require("./json/key.json");
let invites = require("./json/invites.json");
let messages = require("./json/messages.json");

let Discord = require('discord.js');
let client = new Discord.Client();

let currentInvites = {};

let util = require('util');
let wait = require('util').promisify(setTimeout);

client.on('ready', () => {
    wait(1000);
    client.guilds.forEach(g => {
        g.fetchInvites().then(guildInvites => {
            currentInvites[g.id] = guildInvites;
        });
    });
    console.log("Fetched all invites");
});

client.on('guildMemberAdd', member => {
    member.guild.fetchInvites().then(guildInvites => {
        const ei = currentInvites[member.guild.id];
        currentInvites[member.guild.id] = guildInvites;
        const invite = guildInvites.find(i => ei.get(i.code).uses < i.uses);
      });
});

client.on('message', message => {
    if (message.content === '!newInvite') {
        handleNewInvite(message);
    }
    if (message.content === 'ping') message.reply('pong');
});

async function handleNewInvite(message) {
    let invite = await message.channel.createInvite({
        maxAge: 86400,
        maxUses: 1
    },
        `Aangevraagd door: ${message.author.tag}`,
    ).catch(console.log);
    let stringified = JSON.stringify(messages.newInvite);
    stringified = stringified.replace("{invite}", invite);
    message.channel.send(JSON.parse(stringified));
}

client.login(key.key);