/* Copyright 2008, 2009, 2010 by the Oxford University Computing Laboratory

   This file is part of HermiT.

   HermiT is free software: you can redistribute it and/or modify
   it under the terms of the GNU Lesser General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   HermiT is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public License
   along with HermiT.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.semanticweb.HermiT.hierarchy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.HermiT.model.AtomicRole;
import org.semanticweb.HermiT.model.Individual;

public class RoleElementManager {
    
    public static final String LB=System.getProperty("line.separator");
    
    protected final Map<AtomicRole,RoleElement> m_roleToElement;

    
    protected RoleElementManager() {
        m_roleToElement= new HashMap<>();
    }
    public RoleElement getRoleElement(AtomicRole role) {
        if (m_roleToElement.containsKey(role)) 
            return m_roleToElement.get(role);
        else {
            RoleElement element=new RoleElement(role);
            m_roleToElement.put(role, element);
            return element;
        }
    }
    public String toString() {
        StringBuilder buffer=new StringBuilder();
        for (Map.Entry<AtomicRole, RoleElement> entry : m_roleToElement.entrySet()) {
            buffer.append(entry.getKey());
            buffer.append(" -> ");
            buffer.append(entry.getValue().toString());
            buffer.append(LB);
        }
        return buffer.toString();
    }
    
    public class RoleElement {
        protected final AtomicRole m_role;
        protected Map<Individual,Set<Individual>> m_knownRelations;
        protected Map<Individual,Set<Individual>> m_possibleRelations;
        
        protected RoleElement(AtomicRole role) {
            m_role=role;
            m_knownRelations= new HashMap<>();
            m_possibleRelations= new HashMap<>();
        }
        public AtomicRole getRole() {
            return m_role;
        }
        public boolean isKnown(Individual individual1, Individual individual2) {
            return m_knownRelations.containsKey(individual1) && m_knownRelations.get(individual1).contains(individual2);
        }
        public boolean isPossible(Individual individual1, Individual individual2) {
            return m_possibleRelations.containsKey(individual1) && m_possibleRelations.get(individual1).contains(individual2);
        }
        public Map<Individual,Set<Individual>> getKnownRelations() {
            return m_knownRelations;
        }
        public Map<Individual,Set<Individual>> getPossibleRelations() {
            return m_possibleRelations;
        }
        public boolean hasPossibles() {
            return !m_possibleRelations.isEmpty();
        }
        public void setToKnown(Individual individual1, Individual individual2) {
            Set<Individual> successors=m_possibleRelations.get(individual1);
            successors.remove(individual2);
            if (successors.isEmpty())
                m_possibleRelations.remove(individual1);
            addKnown(individual1, individual2);
        }
        public boolean addKnown(Individual individual1, Individual individual2) {
            Set<Individual> successors = m_knownRelations.computeIfAbsent(individual1, k -> new HashSet<>());
            return successors.add(individual2);
        }
        public boolean addKnowns(Individual individual, Set<Individual> individuals) {
            Set<Individual> successors = m_knownRelations.computeIfAbsent(individual, k -> new HashSet<>());
            return successors.addAll(individuals);
        }
        public boolean removeKnown(Individual individual1, Individual individual2) {
            Set<Individual> successors=m_knownRelations.get(individual1);
            boolean removed=false;
            if (successors!=null) {
                removed=successors.remove(individual2);
                if (successors.isEmpty())
                    m_knownRelations.remove(individual1);
            }
            return removed;
        }
        public boolean addPossible(Individual individual1, Individual individual2) {
            Set<Individual> successors = m_possibleRelations.computeIfAbsent(individual1, k -> new HashSet<>());
            return successors.add(individual2);
        }
        public boolean removePossible(Individual individual1, Individual individual2) {
            Set<Individual> successors=m_possibleRelations.get(individual1);
            boolean removed=false;
            if (successors!=null) {
                removed=successors.remove(individual2);
                if (successors.isEmpty())
                    m_possibleRelations.remove(individual1);
            }
            return removed;
        }
        public boolean addPossibles(Individual individual, Set<Individual> individuals) {
            Set<Individual> successors = m_possibleRelations.computeIfAbsent(individual, k -> new HashSet<>());
            return successors.addAll(individuals);
        }
        public String toString() {
            StringBuilder buffer=new StringBuilder();
            buffer.append(m_role);
            buffer.append(" (known instances: ");
            boolean notfirst=false;
            for (Map.Entry<Individual, Set<Individual>> entry : m_knownRelations.entrySet()) {
                for (Individual successor : entry.getValue()) {
                    if (notfirst) { 
                        buffer.append(", ");
                        notfirst=true;
                    }
                    buffer.append("(");
                    buffer.append(entry.getKey().toString());
                    buffer.append(", ");
                    buffer.append(successor.toString());
                    buffer.append(")");
                }
            }
            buffer.append(" | possible instances: ");
            notfirst=false;
            for (Map.Entry<Individual, Set<Individual>> entry : m_possibleRelations.entrySet()) {
                for (Individual successor : entry.getValue()) {
                    if (notfirst) { 
                        buffer.append(", ");
                        notfirst=true;
                    }
                    buffer.append("(");
                    buffer.append(entry.getKey().toString());
                    buffer.append(", ");
                    buffer.append(successor.toString());
                    buffer.append(")");
                }
            }
            buffer.append(") ");
            return buffer.toString();
        }
    }
}

