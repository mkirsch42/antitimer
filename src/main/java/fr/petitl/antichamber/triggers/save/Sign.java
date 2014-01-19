package fr.petitl.antichamber.triggers.save;

import java.util.*;

/**
 *
 */
public enum Sign {
    SIGN_0("Every journey is a series of choices. The first is to begin the journey.", -1), //
    SIGN_1("Patience has it's own rewards.", 56), //
    SIGN_2("Taking the first step can be harder than the rest of the challenge.", 52), //
    SIGN_3("Many small obstacles can make for one large problem.", 74), //
    SIGN_4("Some choices are only useful when we make them early.", 71), //
    SIGN_6("Failing to succeed does not mean failing to progress.", 26), //
    SIGN_5("Live on your own watch, not someone else's.", 53), //
    SIGN_7("Some paths are clearer than others.", 115), //
    SIGN_8("Some paths are straight forward.", 46, 89), //
    SIGN_9("A choice may be as simple as going left or going right.", 130), //
    SIGN_10("The choice doesn't matter if the outcome is the same.", 69), //
    SIGN_11("If you don't like where you've ended up try doing something else.", 169), //
    SIGN_12("When you return to where you have been, things aren't always as remembered.", 123), //
    SIGN_13("We fall down when there is nothing to support us.", 60, 118), //
    SIGN_14("Building a bridge can get you over a problem.", 61), //
    SIGN_15("Some hurdles are too high to jump over.", 15), //
    SIGN_16("Life is full of ups and downs.", 13), //
    SIGN_17("Connecting the pieces can solve a puzzle.", 140), //
    SIGN_18("Life has a way of pushing us in the right direction.", 28), //
    SIGN_19("The end may come before we were ready to get there.", 109), //
    SIGN_20("Life isn't about getting to the end.", 129), //
    SIGN_21("It's harder to progress if you're leaving things behind.", 23), //
    SIGN_22("The further we get, the less help we need.", 24), //
    SIGN_23("Moving forwards may require making the most of what you've got.", 20), //
    SIGN_24("Some tasks require a lot of care and observation.", 93), //
    SIGN_25("Taking one path often means missing out on another.", 122), //
    SIGN_26("A path may not be right or wrong. It may just be different.", 110), //
    SIGN_27("Look a little harder and you will find a way forward.", 44), //
    SIGN_28("Venturing into the unknown can lead to great rewards.", 33), //
    SIGN_29("If you're only focusing on right now, you won't have enough for later.", 78), //
    SIGN_30("A dead end will only stop you if you don't try to move through it.", 49), //
    SIGN_31("Going a certain way may require building your own path.", 114), //
    SIGN_32("There's nothing wrong with taking shortcuts.", 67), //
    SIGN_33("We often fall into things when we least expect.", 58), //
    SIGN_34("What looks out of reach may only be a few steps away.", 50), //
    SIGN_35("There are multiple ways to approach a situation.", 19), //
    SIGN_36("When you've hit rock bottom, the only way is up.", 48), //
    SIGN_37("Some outcomes are more favourable than others.", 2), //
    SIGN_38("How we perceive a problem can change every time we see it.", 38), //
    SIGN_39("The solution to a problem may just required a more thorough look at it.", 18), //
    SIGN_40("Rushing through a problem won't always give the right results.", 131), //
    SIGN_41("Moving through a problem slower may help find the solution.", 17), //
    SIGN_42("If you never stop trying you will get there eventually.", 82), //
    SIGN_43("Raw persistance may be the only option other than giving up entirely.", 125), //
    SIGN_44("Some problems just come down to size.", 31), //
    SIGN_45("A problem may only be difficult when you are missing the right tools.", 7), //
    SIGN_46("What we've done before may impact what we can do next.", 9), //
    SIGN_47("If you aren't paying attention, you will miss everything around you.", 163), //
    SIGN_48("Some choices can leave us running around in circles.", 124), //
    SIGN_49("Splitting a problem up may help you find the answer.", 160), //
    SIGN_50("What appears impossible may have a very simple answer.", 62), //
    SIGN_51("If you lose sight of what's important, it may not be there when you need it.", 81), //
    SIGN_52("Too much curiosity can get the best of us.", 88), //
    SIGN_53("Signs may be helping you more than you realise.", 64), //
    SIGN_54("Understanding a problem requires filling in the pieces.", 22), //
    SIGN_55("Small steps can take you great distances.", 27), //
    SIGN_56("The path of least resistance is a valid option.", 96), //
    SIGN_57("No matter how high you climb, there's always more to achieve.", 43), //
    SIGN_58("Falling down teaches us how to get up and try again.", 12), //
    SIGN_59("The world rarely changes when we watch to see it happen.", 86), //
    SIGN_60("Half way through is half way finished.", 25), //
    SIGN_61("The problem may not be where you're going but how to get there.", 157), //
    SIGN_62("New skills enable further progress.", 8), //
    SIGN_63("Mastering a skill requires practice.", 116), //
    SIGN_64("A little kind direction can get obstacles out of your way.", 80), //
    SIGN_65("Solving a problem may require approaching it from a different angle.", 136), //
    SIGN_66("Try hard enough and you will get to where you want to be.", 98), //
    SIGN_67("Some doors will close unless we hold them open.", 37), //
    SIGN_68("When you absorb your surroundings you may notice things you didn't see before.", 65), //
    SIGN_69("Dig a little deeper and you may find something new.", 87, 148, 149, 150, 151, 152, 153, 154), //
    SIGN_70("Attention to detail can lead to very rewarding outcomes.", 162), //
    SIGN_71("Solving a problem may require using abilities that we didn't know we had.", 104), //
    SIGN_72("Old skills are useful even after we have learned new ones.", 39), //
    SIGN_73("The right answers may also be the most obvious ones.", 40), //
    SIGN_74("Some obstacles are more stubborn than others.", 45), //
    SIGN_75("Some things don't have a deeper meaning.", 55, 120), //
    SIGN_76("The right decisions at the right time will get you where you want to go.", 83), //
    SIGN_77("Some choices leave us running around a lot without really getting anywhere.", 6), //
    SIGN_78("With forethought, things have a way of just working themselves out.", 66), //
    SIGN_79("Some challenges are far harder than they first appear.", 164), //
    SIGN_80("To get past a problem, you may just need to keep pushing through it.", 166), //
    SIGN_81("Getting to a solution requires cutting out what doesn't work.", 76), //
    SIGN_82("The further we explore, the more connected everything becomes.", 35, 41, 134, 135), //
    SIGN_83("A window of opportunity can lead to new places if you're willing to take a closer look.", 92), //
    SIGN_84("The world looks different on the other side.", 79), //
    SIGN_85("When you have enough resources, you can start growing more.", 144), //
    SIGN_86("You can grow a garden anywhere.", 113), //
    SIGN_87("The best solutions may still be the most primitive ones.", 84), //
    SIGN_88("If you lead the way others will follow.", 32), //
    SIGN_89("Straightforward problems can often require roundabout solutions.", 30), //
    SIGN_90("If you are missing information, it's easy to be mislead.", 70), //
    SIGN_91("When you look beyond the surface, there may be more to find.", 16), //
    SIGN_92("Sometimes we only have just enough to get by.", 59), //
    SIGN_93("When what you have is not enough, find ways to turn it into more.", 77), //
    SIGN_94("Getting where we want may require jumping through some hoops.", 167), //
    SIGN_95("Throwing yourself into things can take you to new heights.", 165), //
    SIGN_96("Similar problems can have entirely different solutions.", 91), //
    SIGN_97("Some events happen whether we want them to or not.", 146), //
    SIGN_98("Old solutions can apply to new problems.", 105, 107, 126, 127), //
    SIGN_99("The consequences of one choice can cut us off from making others.", 72), //
    SIGN_100("At times we need to view the world from someone else's perspective.", 57), //
    SIGN_101("Sometimes you need to be carried.", 128), //
    SIGN_102("There comes a time when you can work your way through anything.", 51), //
    SIGN_103("With more experience previous challenges aren't so difficult.", 73), //
    SIGN_104("There's no need to take apart what already works.", 108), //
    SIGN_105("A few steps backwards may keep you moving forwards.", 47), //
    SIGN_106("Obscure problems may require unusual solutions.", 121), //
    SIGN_107("Getting to the end requires tying off the loose ends.", 42), //
    SIGN_108("The world is always finding new ways to surprise us.", 14), //
    SIGN_109("Sometimes we do things just to go along for the ride.", 36), //
    SIGN_110("The more we complete, the harder it gets to find what we missed.", 133), //
    SIGN_111("Some challenges exist just to test how much we know.", 5), //
    SIGN_112("You can't do everything yourself.", 103), //
    SIGN_113("The world is full of secret waiting to be uncovered.", 3), //
    SIGN_114("Peeking behind the curtains lets us see how everything works.", 106, 142), //
    SIGN_115("Some problems can't be solved until you're more experienced.", 99), //
    SIGN_116("We move on when there is nothing left to learn.", 100), //
    SIGN_117("Complicated problems are easier when solved one step at a time.", 54), //
    SIGN_118("We can appreciate the entire journey by looking back at how far we have come.", 119), //
    SIGN_119("Every journey comes to an end.", 168), //
    ;
    private Set<Integer> ids;

    private static Map<Integer, Sign> valueMap = new HashMap<>();
    static {
        Sign[] values = values();
        for (int i = 0; i < values.length; i++) {
            Sign t = values[i];
            t.pos = i;
            for (Integer id : t.ids) {
                valueMap.put(id, t);
            }
        }
    }

    private String label;
    private int pos;

    Sign(String label, Integer... ids) {
        this.label = label;
        this.ids = new HashSet<>(Arrays.asList(ids));
    }

    public Set<Integer> getIds() {
        return ids;
    }

    public String getLabel() {
        return label;
    }

    public static Sign fromString(String name) {
        String[] split = name.split("_");
        return valueMap.get(Integer.parseInt(split[1]));
    }

    @Override
    public String toString() {
        return "#"+(ordinal()+1)+" "+label;
    }
}
