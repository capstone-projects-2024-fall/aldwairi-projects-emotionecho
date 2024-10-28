# input: list of name indicating the emotion, E.x. [1,2,3,4,5,6,6,7]
# return: list of emotion percentage in order, E.x. [12.5, 12.5, 12.5, 12.5, 12.5, 25.0, 12.5]
def getEmotions(listOfName){
    def getEmotions(listOfName):
        emotions = [0.0,0.0,0.0,0.0,0.0,0.0,0.0]
        for num in listOfName:
            emotions[num-1] += 1
        for index,num in enumerate(emotions):
            emotions[index] = num/len(listOfName)*100
        return emotions
}