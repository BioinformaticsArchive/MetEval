'''
Created on Jul 25, 2011

@author: Piotr Zakrzewski
'''
import sys
import os.path
import xml.dom.minidom

externals = list()
def addExternal(externalMetabolite):
    global externals
    if not externalMetabolite in externals:
        externals.append(externalMetabolite)
class SurreyReaction:
##    def __init__(self, name, gene, reversible):
##        self.name = name
##        self.gene = gene
##        self.reversible = reversible
##        self.products = dict()
##        self.substrates = dict()
##        self.lowerBound = "-10000"
##        self.upperBound = "10000"
    def __init__(self,reactionNode):
        self.name = reactionNode.attributes.getNamedItem('id').nodeValue
        self.comment = ""
        self.comment = reactionNode.attributes.getNamedItem('name').nodeValue 
        reactants = reactionNode.getElementsByTagName('listOfReactants')
        self.__parseReactants__(reactants)
        products  = reactionNode.getElementsByTagName('listOfProducts')
        self.__parseProducts__(products)
        if self.__hasKineticLaw__(reactionNode):
            kineticLaw = reactionNode.getElementsByTagName('kineticLaw')[0]
            self.__parseParameters__(kineticLaw)
        else:
            self.upperBound = "10000"
            self.lowerBound = "10000"
        notes = reactionNode.getElementsByTagName("notes")[0]
        self.enzyme = ""
        self.__parseNotes__(notes)
    def __hasKineticLaw__(self,node):
        kNodes = node.getElementsByTagName("kineticLaw")
        if len(kNodes) == 0:
            return False
        else:
            return True
    def __parseReactants__(self,reactants):
        self.reactants = dict()
        reactants = reactants[0].getElementsByTagName('speciesReference')
        for species in reactants:
            name = species.attributes.getNamedItem('species').nodeValue
            if self.__containtsAttribute__(species,'stoichiometry'):
                stochiometry = species.attributes.getNamedItem('stoichiometry').nodeValue
            else:
                stochiometry = "1"
            self.reactants[name] = stochiometry
    def __containtsAttribute__(self,node,attributeName):
        for name in node.attributes.keys():
            if name == attributeName:
                return True
        return False 
    def __parseProducts__(self,products):
        self.products = dict()
        products = products[0].getElementsByTagName('speciesReference')
        for species in products:
            name = species.attributes.getNamedItem('species').nodeValue
            if self.__containtsAttribute__(species,'stoichiometry'):
                stochiometry = species.attributes.getNamedItem('stoichiometry').nodeValue
            else:
                stochiometry = "1"
            self.products[name] = stochiometry
    def __parseParameters__(self,kineticLaw):
        params = kineticLaw.getElementsByTagName('parameter')
        for param in params:
            name = param.attributes.getNamedItem('id').nodeValue
            value = param.attributes.getNamedItem('value').nodeValue
            if name == 'LOWER_BOUND':
                self.lowerBound = value
            elif name == 'UPPER_BOUND':
                self.upperBound = value
    def __parseNotes__(self,notes):
        entries = notes.getElementsByTagName("html:p")
        for entry in entries:
            text = entry.firstChild.nodeValue
            if text.find("PROTEIN_CLASS:") > -1:
                self.enzyme = text.replace("PROTEIN_CLASS:","")
        
    def __str__(self):
        text = self.name
        text += "\t"
        text += self.__equation2str__()
        text += "\t"
        text += self.lowerBound
        text += "\t"
        text += self.upperBound
        text += '\t'
        text += self.enzyme
        text += '\t'
        text += "#"+self.comment
        return text
    def __equation2str__(self):
        text = ""
        for substrate,quantity in self.reactants.items():
            coefficient = self.__coerceCoefficient__(quantity)
            name = self.__sanitizeName__(substrate)
            name = self.__checkBoundraryCondition__(name)
            text += "{0} {1} + ".format(coefficient,name)
        text = self.__trimEquation__(text)
        text += "= "
        for product,quantity in self.products.items():
            coefficient = self.__coerceCoefficient__(quantity)
            name = self.__sanitizeName__(product)
            name = self.__checkBoundraryCondition__(name)
            text += "{0} {1} + ".format(coefficient,name)
        text = self.__trimEquation__(text)
        return text
    def __coerceCoefficient__(self,coefficient):
        if coefficient == "1":
            return ""
        elif not coefficient.find(".") > -1:
            return coefficient+".0"
        else:
            return coefficient
    def __sanitizeName__(self,name):
        if not name.isalnum():
            newName = ""
            for char in name:
                if char.isalnum():
                    newName += char
                else:
                    newName += "_"
            #print("Warning",name,"invalid. changed to:",newName)
            return newName
        else:
            return name 
    def __checkBoundraryCondition__(self,metabolite):
        if self.__isExternal__(metabolite):
            return self.__externalize__(metabolite)
        else:
            return metabolite
    def __isExternal__(self,metabolite):
        if metabolite[-2:] == "_b":
            return True
        else:
            return False
    def __externalize__(self,metabolite):
        newName = metabolite.replace("_b","_ext")
        addExternal(newName)
        return    newName
    def __trimEquation__(self,equationPart):
        lastPlus = equationPart.rfind("+")
        return equationPart[0:lastPlus]

def checkScriptParameters():
    if not len(sys.argv) == 3:
        print("parseSbml requires two parameters: path to input (sbml file) and output path")
        return False
    else:
        global inputPath, outputPath
        inputPath = sys.argv[1]
        outputPath = sys.argv[2]
        return True
def checkXmlExtension(filePath):
    fileName = os.path.basename(filePath)
    splitage = fileName.split(".")
    extension = ""
    if len(splitage) < 2:
        return False
    else:
        extension = splitage[1]
    if extension == "xml" or extension == "sbml":
        return True
    else:
        return False
def isExternal(tagName):
    pass
def parse(xmlFile):
    reactionList = getReactions(xmlFile)
    return reactionList
def getReactions(xmlFile):
    reactionList = xmlFile.getElementsByTagName('reaction')
    surreyReactions = list()
    for reactionNode in reactionList:
        newReact = SurreyReaction(reactionNode)
        surreyReactions.append(newReact)
    return surreyReactions
def writeLog():
    logFile = open("parseSbml log.log","a")
    logFile.write(inputPath +" "+outputPath)
    logFile.close()
             
def writeExternals():
    oFile = open(outputPath+"_ext","w")
    oFile.write("\\")
    for ext in externals:
        oFile.write(ext+" ")
    #print("found",len(externals),"external metabolites")
    oFile.close()
if checkScriptParameters():
    writeLog()
    sbml = open(inputPath,"r")
    sbmlXml = xml.dom.minidom.parse(sbml)
    surreyFormat = parse(sbmlXml)
    sbml.close()
    outputFile = open(outputPath,"w")
    for reaction in surreyFormat:
        outputFile.write(str(reaction)+"\n")
    outputFile.close()
    writeExternals()
    
    
