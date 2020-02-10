let key = require("./json/key.json");
let invites = require("./json/invites.json");
let messages = require("./json/messages.json");
let schedule = require('node-schedule');
let meme = require("./meme.js")

let Discord = require('discord.js');
let client = new Discord.Client();

let currentInvites = {};

let util = require('util');
let wait = require('util').promisify(setTimeout);

//test

process.on('message', function (msg) {
    if (msg === 'shutdown') {
        let stoppingMessage = JSON.stringify(messages.stopping);
        stoppingMessage = stoppingMessage.replace("{timestamp}", new Date());
        client.channels.get("676395265443364874").send(JSON.parse(stoppingMessage));
        setTimeout(function () {
            console.log('Finished closing connections');
            process.exit(0);
        }, 1000);
    }
});

client.on('ready', () => {
    let startedMessage = JSON.stringify(messages.started);
    startedMessage = startedMessage.replace("{timestamp}", new Date());
    wait(1000);
    client.channels.get("676395265443364874").send(JSON.parse(startedMessage));
    client.guilds.forEach(g => {
        g.fetchInvites().then(guildInvites => {
            currentInvites[g.id] = guildInvites;
        });
    });
});

client.on('guildMemberAdd', member => {
    member.guild.fetchInvites().then(guildInvites => {
        const ei = currentInvites[member.guild.id];
        currentInvites[member.guild.id] = guildInvites;
        const invite = guildInvites.find(i => ei.get(i.code).uses < i.uses);
    });
});

client.on('message', message => {
    if (message.content === "!ping") message.reply('Wassupp nibbas!');
    if (message.content === '!newInvite') handleNewInvite(message);
    if (message.content === '!herres') message.reply('HAHAHAHHA HIJ ZEI HERRES!!!!');
    if (message.content === '!sendMeme') handleMeme(message);
});

async function handleMeme(message) {
    let sent = await message.channel.send("Meme is aan het laden...");
    let id = sent.id;
    meme.getMemeJSON((data) => {
        message.channel.fetchMessages({ around: id, limit: 1 }).then((foundMessages) => {
            let editableMessage = foundMessages.first();
            let memeMessage = JSON.stringify(messages.meme);
            memeMessage = memeMessage.replace("{title}", data.title);
            memeMessage = memeMessage.replace("{subreddit}", data.subreddit);
            memeMessage = memeMessage.replace("{url}", data.url);
            memeMessage = memeMessage.replace("{postlink}", data.postLink);
            editableMessage.edit(JSON.parse(memeMessage));
        });
    });
}

async function handleNewInvite(message) {
    let invite = await message.channel.createInvite({
        maxAge: 86400,
        maxUses: 1,
        unique: true
    },
        `Aangevraagd door: ${message.author.tag}`,
    ).catch(console.log);
    let stringified = JSON.stringify(messages.newInvite);
    stringified = stringified.replace("{invite}", invite);
    message.channel.send(JSON.parse(stringified));
}

var j = schedule.scheduleJob('30 * * * *', () => {
    client.channels.get('675434903533387809').send("Dit is een bericht dat elk half uur wordt verstuurd");
});

client.login(key.key);
