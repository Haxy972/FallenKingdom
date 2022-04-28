package fr.haxy972.fallen.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageYaml {


    public static void checkYaml() {
        File file = new File("plugins/FallenKingdom/message.yml");


        try {
            if (!file.isFile()) {
                file.createNewFile();
                YamlConfiguration yaml = new YamlConfiguration();

                //yaml.set("messages.nether.new-day-nether", "");

                yaml.set("messages.partie.restart", "&cRedémarrage du serveur");
                yaml.set("messages.partie.start-title", "&6FallenKingdom");
                yaml.set("messages.partie.start-subtitle", "&7Début de la partie");
                yaml.set("messages.partie.no-winner", "&cAucune équipe n'a gagné");
                yaml.set("messages.partie.team-win", "&7L'équipe {team} &7a gagné !");
                yaml.set("messages.partie.chest-refill", "&aLes coffres du centre de la carte ont été &eréapprovisionnés");
                yaml.set("messages.partie.lobby.block-place-break", "&c&lErreur&8» &7Cette action est impossible, partie non débutée");
                yaml.set("messages.partie.lobby.start-aborted", "&cLancement annulé, un joueur s'est déconnecté/connecté");
                yaml.set("messages.partie.lobby.timer-countdown", "&7La partie commence dans &b&l{nombre} {secondes}");
                yaml.set("messages.partie.lobby.too-far", "&cNe vous éloignez pas trop du spawn :c");



                yaml.set("messages.team.default-team", "&7Vous serez spectateur dans cette partie, vous n'avez choisi aucune équipe");
                yaml.set("messages.team.disabled", "&cCette équipe a été désactivée pour des raisons de performances");
                yaml.set("messages.team.joined", "&7Vous avez rejoint l'équipe {team}");
                yaml.set("messages.team.already", "&c&lERREUR&8» &7Vous êtes déjà dans cette équipe");
                yaml.set("messages.team.full", "&c&lERREUR&8» &7L'équipe {team} &7est pleine");
                yaml.set("messages.team.leave", "&7Vous avez quitté votre équipe");
                yaml.set("messages.team.team-less", "&c&lERREUR&8» &7Vous n'êtes pas dans une équipe");
                yaml.set("messages.team.bed-destroyed", "&cVous ne pouvez plus réapparaitre, votre nexus est détruit");
                yaml.set("messages.team.cant-respawn", "&c&lERREUR&8» &7Votre nexus a été détruit, réapparition impossible");
                yaml.set("messages.death.title", "&c&lMort");
                yaml.set("messages.death.subtitle", "&7Vous allez réapparaître");
                yaml.set("messages.nether.error-nether", "&c&lERREUR&8» &7Les portails ne sont pas encore activés &c(JOUR {jour})");
                yaml.set("messages.tnt.day-error", "&cVous ne pouvez pas poser de TNT&e(JOUR {jour})");
                yaml.set("messages.blocks.place-error", "&cVous ne pouvez pas poser de blocs ici !");
                yaml.set("messages.blocks.unbreakable", "&cCe bloc est incassable");
                yaml.set("messages.blocks.breakable-by-pickaxe", "&cVous ne pouvez casser que le beacon &e(PIOCHE)");
                yaml.set("messages.blocks.own-beacon", "&cVous ne pouvez pas casser votre propre beacon");
                yaml.set("messages.blocks.region-marge-error", "&cVous ne pouvez pas construire &e&lautour &ede la base des autres");
                yaml.set("messages.pvp.assault-error", "&c&lERREUR&8» &7Assault de base désactivé &c(Jour {jour})");
                yaml.set("messages.pvp.pvp-disabled", "&7Le &c&lPvP&7 est actuellement désactivé &c(Jour {jour})");
                yaml.set("messages.days.new", "&aUn nouveau jour se lève &7(Jour n° {jour})");
                yaml.set("messages.days.pvp-activated", "&7Le &c&lPvP &7est désormais activé");
                yaml.set("messages.days.nether-activated", "&7Le &c&lNether &7est désormais activé");
                yaml.set("messages.days.feed-less", "&cVous perdez désormais de la nourriture");
                yaml.set("messages.days.annonce-to-be-destroyed", "&cLe jour suivant les lits seront détruits");
                yaml.set("messages.days.bed-destroyed", "&cTous les lits ont été détruits");



                yaml.save(file);


                System.out.println("§cDEBUG: §emessage.yml: recreated");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getValue(String key) {


        try {
            File file = new File("plugins/FallenKingdom/message.yml");
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.load(file);
            return yaml.getString(key);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "§c§lINTERNAL-ERROR§8» §7Une erreur de configuration est survenue !";
    }

}






