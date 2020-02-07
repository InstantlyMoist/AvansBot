let Discord = require('discord.js');
let client = new Discord.Client();

let invites = {};

let util = require('util');
let wait = require('util').promisify(setTimeout);

client.on('ready', () => {
   wait(1000);
   client.guilds.forEach(g => {
    g.fetchInvites().then(guildInvites => {
      invites[g.id] = guildInvites;
    });
  });
   console.log("Fetched all invites");
});

client.on('guildMemberAdd', member => {
    member.guild.fetchInvites().then(guildInvites => {
        const ei = invites[member.guild.id];
        invites[member.guild.id] = guildInvites;
        const invite = guildInvites.find(i => ei.get(i.code).uses < i.uses);
        if (invite.code === "H4D4EwM") member.guild.channels.get("675434903533387809").send("jij bent een sv turning knaap");
        else member.guild.channels.get("675434903533387809").send("andere invite link");
    });
});

client.on('message', message => {
    if (message.content === 'ping') message.reply('pong');
});

