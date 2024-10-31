# input: list of name indicating the emotion, E.x. [1,2,3,4,5,6,6,7]
# return: list of emotion percentage in order, E.x. [12.5, 12.5, 12.5, 12.5, 12.5, 25.0, 12.5]
def getEmotionsPercentage(listOfFileName){
    listOfEmotion = getEmotions(listOfFileName);
    emotionsPercentage = [0.0,0.0,0.0,0.0,0.0,0.0,0.0]
    for num in listOfEmotion:
        emotionsPercentage[num-1] += 1
    for index,num in enumerate(emotionsPercentage):
        emotionsPercentage[index] = num/len(listOfEmotion)*100
    return emotionsPercentage
}

def getEmotions(listOfFileName){
    listOfEmotion = list([])
    for name in listOfFileName:
        listOfEmotion.append(name[7])
    return listOfEmotion
}