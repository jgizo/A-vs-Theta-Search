import os
os.system("python3 -m pip install pygame")

import pygame, sys, random
from PyQt5 import QtWidgets
from pygame.locals import *
desktop = QtWidgets.QApplication(sys.argv)
desktopScreen = desktop.desktop().screenGeometry()
h = desktopScreen.height()
w = desktopScreen.width()

# Define some colors
BLACK = (0, 0, 0)
WHITE = (255, 255, 255)
GREEN = (0, 255, 0)
RED = (255, 0, 0)
BLUE = (0, 0, 255)

astar_ghvals = []
thetastar_ghvals = []

grid = []
astar_path = []
thetastar_path = []

pygame.init()
base_font = pygame.font.Font(None, 32)

screen = pygame.display.set_mode([.95*w, .95*h])
pygame.display.set_caption('A* Grid')

start = []
goal = []

def readGrid(fileName):

    file = open(fileName, 'r')

    global start;
    global goal;
    global astar_path;
    global thetastar_path;
    global astar_ghvals;
    global thetastar_ghvals;

    start = file.readline().strip().split()
    goal = file.readline().strip().split()
    dimension = file.readline().strip().split()
    fileLines = file.readlines()

    grid = [[0 for i in range(int(dimension[0]))] for j in range(int(dimension[1]))]

    hit_astar_path = False;
    hit_thetastar_path = False;
    hit_astar_vals = False;
    hit_thetastar_vals = False;
    count = 0

    for line in fileLines:
        line = line.strip().split()
        count += 1

        if line[0] == "&":
            hit_astar_vals = True
            hit_thetastar_path = False
            hit_astar_path = False
            continue

        if line[0] == "$":
            hit_thetastar_vals = True
            hit_astar_vals = False
            hit_thetastar_path = False
            hit_astar_path = False
            continue

        if line[0] == "=":
            hit_astar_path = True
            continue

        if line[0] == "+":
            hit_thetastar_path = True
            hit_astar_path = False
            continue

        if hit_astar_path:
            astar_path.append(line)
            continue

        if hit_thetastar_path:
            thetastar_path.append(line)
            continue

        if hit_astar_vals:
            astar_ghvals.append(line)
            continue

        if hit_thetastar_vals:
            thetastar_ghvals.append(line)
            continue

        if not hit_thetastar_path or hit_astar_path or hit_astar_vals or hit_thetastar_vals:
            grid[int(line[1])-1][int(line[0])-1] = int(line[2])

    return grid

def drawGrid(grid, width, numCols, numRows):
    cellSize = (.8*min(w, h))/min(numCols, numRows)
    global start;
    global goal;

    coordinate_font = pygame.font.Font(None, int(min(w,h)/min(numRows, numCols)))

    for x in range(0, len(grid)):
        for y in range(0, len(grid[x])):
            cell = pygame.Rect(y*cellSize, x*cellSize, cellSize, cellSize)

            if(x==len(grid)-1):
                coordinate = coordinate_font.render(str(y + 1), False, RED)
                screen.blit(coordinate, ((y-.5) * cellSize, (x+1) * cellSize))

            if grid[x][y] == 0:
                pygame.draw.rect(screen, BLACK, cell, 1)
            else:
                pygame.draw.rect(screen, BLACK, cell)

        coordinate = coordinate_font.render(str(x+1), False, RED)
        screen.blit(coordinate, ((y+1)*cellSize, (x-.5)*cellSize))
        if(y == len(grid[x])):
            coordinate = coordinate_font.render(str(x + 3), False, RED)
            screen.blit(coordinate, ((y + 1) * cellSize, (x - .5) * cellSize))



    #Adds red and green circles
    pygame.draw.circle(screen, GREEN, [(int(start[0])-1) * cellSize, (int(start[1])-1) * cellSize], cellSize/3)
    pygame.draw.circle(screen, RED, [(int(goal[0])-1) * cellSize, (int(goal[1])-1) * cellSize], cellSize/3)

def drawThetaStar(width, numCols, numRows):
    cellSize = (.8*min(w, h))/min(numCols, numRows)
    for x in range(0, len(thetastar_path)-1):
        pygame.draw.line(screen, RED,
                         [(int(thetastar_path[x + 1][0]) - 1) * cellSize, (int(thetastar_path[x + 1][1]) - 1) * cellSize],
                         [(int(thetastar_path[x][0]) - 1) * cellSize, (int(thetastar_path[x][1]) - 1) * cellSize], 3)

def drawAStar(width, numCols, numRows):
    cellSize = (.8*min(w, h))/min(numCols, numRows)

    for x in range(0, len(astar_path)-1):
        pygame.draw.line(screen, BLUE,  [(int(astar_path[x][0]) - 1) * cellSize, (int(astar_path[x][1]) - 1) * cellSize],
                         [(int(astar_path[x+1][0]) - 1) * cellSize, (int(astar_path[x+1][1]) - 1) * cellSize], 3)

def deletePaths(grid, width, numCols, numRows):
    cellSize = (.8*min(w, h))/min(numCols, numRows)
    for x in range(0, len(thetastar_path) - 1):
        pygame.draw.line(screen, WHITE,
                         [(int(thetastar_path[x + 1][0]) - 1) * cellSize,
                          (int(thetastar_path[x + 1][1]) - 1) * cellSize],
                         [(int(thetastar_path[x][0]) - 1) * cellSize, (int(thetastar_path[x][1]) - 1) * cellSize], 5)
    for x in range(0, len(astar_path)-1):
        pygame.draw.line(screen, WHITE,  [(int(astar_path[x+1][0]) - 1) * cellSize, (int(astar_path[x+1][1]) - 1) * cellSize],
                                        [(int(astar_path[x][0]) - 1) * cellSize, (int(astar_path[x][1]) - 1) * cellSize]    , 5)

    global start;
    global goal;

    for x in range(0, len(grid)):
        for y in range(0, len(grid[x])):
            cell = pygame.Rect(y * cellSize, x * cellSize, cellSize, cellSize)
            if grid[x][y] == 0:
                pygame.draw.rect(screen, BLACK, cell, 1)
            else:
                pygame.draw.rect(screen, BLACK, cell)
    # Adds red and green circles

    pygame.draw.circle(screen, GREEN, [(int(start[0]) - 1) * cellSize, (int(start[1]) - 1) * cellSize], cellSize / 3)
    pygame.draw.circle(screen, RED, [(int(goal[0]) - 1) * cellSize, (int(goal[1]) - 1) * cellSize], cellSize / 3)

def displayValues(coordinate):
    global astar_ghvals;
    global thetastar_ghvals;

    foundAstarVal = False
    foundThetastarVal = False

    astarLine = []
    thetastarLine = []

    coordinate = coordinate.strip().split()

    for line in astar_ghvals:
        if float(line[0]) == float(coordinate[0]) and float(line[1]) == float(coordinate[1]):
            foundAstarVal = True
            astarLine = line
            break

    for line in thetastar_ghvals:
        if float(line[0]) == float(coordinate[0]) and float(line[1]) == float(coordinate[1]):
            foundThetastarVal = True
            thetastarLine = line
            break

    if foundThetastarVal:
        t_text = f"Theta* Values: g(c)={thetastarLine[2]}, h(c)={thetastarLine[3]}, f(c)={float(thetastarLine[2]) + float(thetastarLine[3])}"
    else:
        t_text = "Node not visited during Theta* search"

    if foundAstarVal:
        a_text = f"A* Values: g(c)={astarLine[2]}, h(c)={astarLine[3]}, f(c)={float(astarLine[2]) + float(astarLine[3])}"
    else:
        a_text = "Node not visited during A* search"

    thetastar_vals = base_font.render(t_text, False, RED)
    astar_vals = base_font.render(a_text, False, BLUE)

    erased = pygame.Rect(w/10, h*.85, w, h * .2)
    pygame.draw.rect(screen, WHITE, erased)

    screen.blit(thetastar_vals, (w/10, h*.85))
    screen.blit(astar_vals, (w/10, h*.875))

fileName = sys.argv

grid = readGrid(str(fileName[1]))

astar_path = [i for i in astar_path if i != 0]


screen.fill(WHITE)
drawGrid(grid, max(w, h), len(grid[1]), len(grid))
input_rect = pygame.Rect(abs(w*.8), abs(.85*h), 140, 32)
user_text = ''

while True:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            sys.exit()

        if event.type == pygame.MOUSEBUTTONDOWN:
            if input_rect.collidepoint(event.pos):
                active = True
            else:
                active = False

        #Display text box and check if user text was submitted
        if event.type == pygame.KEYDOWN:
            # Check for backspace
            if event.key == pygame.K_BACKSPACE:
                # get text input from 0 to -1 i.e. end.
                user_text = user_text[:-1]
            # Unicode standard is used for string
            # formation
            elif event.key == pygame.K_RETURN:
                displayValues(user_text)
            elif event.key == pygame.K_t:
                drawThetaStar(w, len(grid[1]), len(grid))
            elif event.key == pygame.K_a:
                drawAStar(w, len(grid[1]), len(grid))
            elif event.key == pygame.K_DELETE:
                deletePaths(grid, w, len(grid[1]), len(grid))
            else:
                user_text += event.unicode

    pygame.draw.rect(screen, BLACK, input_rect)
    text_surface = base_font.render(user_text, True, (255, 255, 255))
    screen.blit(text_surface, (input_rect.x + 5, input_rect.y + 5))

    pygame.display.update()